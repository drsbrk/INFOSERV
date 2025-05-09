package bi.gov.obr.informationServices.service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.entity.AppEventLogger;
import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.repository.AppEventLoggerRepository;
import bi.gov.obr.informationServices.repository.ExerciceRepository;
import bi.gov.obr.informationServices.repository.PrevisionRepository;
import bi.gov.obr.informationServices.utils.Helper;
import bi.gov.obr.informationServices.utils.Paged;
import bi.gov.obr.informationServices.utils.Paging;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExerciceServiceImpl implements ExerciceService {

  private final ExerciceRepository exerciceRepository;
  private final AppEventLoggerRepository appEventLoggerRepository;
  private final PrevisionRepository previsionRepository;

  public ExerciceServiceImpl(ExerciceRepository exerciceRepository, AppEventLoggerRepository appEventLoggerRepository, PrevisionRepository previsionRepository) {
    this.exerciceRepository = exerciceRepository;
    this.appEventLoggerRepository = appEventLoggerRepository;
    this.previsionRepository = previsionRepository;
  }

  @Override
  public boolean deleteExercice(Long idExercice) {
    Exercice exercice = exerciceRepository
        .findById(idExercice).orElseThrow( () -> new IllegalArgumentException("L'exercice n'existe pas"));

    if(previsionRepository.findOneByAnneeBudgetaire(exercice) == null) {
      exerciceRepository.deleteById(idExercice);
    } else {
      throw new IllegalArgumentException("L'année budgétaire es déjà utilisée dans une prévision des recettes");
    }
    return true;
  }

  @Override
  public List<Exercice> findAllExercice() {
    List<Exercice> exercices = exerciceRepository.findAllByOrderByDateDebutDesc();
    return exercices;
  }

  /*
   * @Override public List<Exercice> findAllExercice() { List<Exercice> exercices
   * = exerciceRepository.findAllByOrderByIdDesc(); return exercices; }
   */

  @Override
  public Paged<Exercice> findAllExercice(Pageable pageable) {
    Page<Exercice> exercicePage = exerciceRepository.findAllByOrderByIdDesc(pageable);
    return new Paged<>(exercicePage,
        Paging.of(exercicePage.getTotalPages(),
            pageable.getPageNumber(), pageable.getPageSize()));
  }

  @Override
  public Exercice findById(Long idExercice) {
    return exerciceRepository.findById(idExercice).orElseThrow(
        () -> new EntityNotFoundException("Année budgetaire n'existe pas."));
  }

  @Override
  public Exercice findCurrentAnneeBudgetaire() {
    List<Exercice> exercices = exerciceRepository.findExerciceByBetweenDateDebutAnAndDateFin(LocalDate.now());
    if (exercices != null && !exercices.isEmpty()) {
      return exercices.get(0);
    }
    throw new EntityNotFoundException("Il n'y a pas d'exercice courant.");
  }

  @Override
  public Exercice findExerciceByDateBetween(LocalDate currentDate) {
    return exerciceRepository.findByBetweenDateDebutAnAndDateFin(currentDate).orElseThrow(
        () -> new EntityNotFoundException("Il n'y a pas d'exercice courant.")
        );
  }

  @Override
  public Exercice saveExercice(Exercice exercice, AppEventLogger appEventLogger) {

    try {
      Year year = Year.of(Integer.parseInt(exercice.getCode()));
    } catch (Exception exception){
      throw new IllegalArgumentException("Mettez l'année comme code de l'année budgétaire");
    }

    Long existingId = exerciceRepository.findIdByCode(exercice.getCode());
    if (existingId != null) {
      throw new EntityExistsException("Le code de l'année budgétaire existe déjà");
    }

    if (exerciceRepository.findExerciceIdByBetweenDateDebutAnAndDateFin(exercice.getDateDebut()).isPresent()) {
      throw new
      EntityExistsException("La date de " +
          "début de l'exercice se trouve dans l'interval d'un autre exercice");
    }

    if (exerciceRepository.findExerciceIdByBetweenDateDebutAnAndDateFin(exercice.getDateFin()).isPresent()) {
      throw new EntityExistsException("La date de fin de l'exercice se trouve dans l'interval d'un autre exercice");
    }

    exercice = exerciceRepository.save(exercice);

    if (exercice.getId() != null) {
      String appEventLoggerJson = Helper.getJsonString(exercice);
      System.out.println("AppEventLogger in JSON format : " + appEventLoggerJson);
      appEventLogger.setEntityJson(appEventLoggerJson);
      appEventLoggerRepository.save(appEventLogger);
    }
    return exercice;
  }

  @Override
  public Exercice updateExercice(Long idExercice, Exercice exercice, AppEventLogger appEventLogger) {
    if (idExercice == null || exercice.getId() == null) {
      throw new IllegalArgumentException("The identifier for the exercise is null");
    }
    if (idExercice.longValue() != exercice.getId().longValue()) {
      throw new IllegalArgumentException("The identifier for the exercise does not match the identifier of the exercise object");
    }

    Long existingId = exerciceRepository.findIdByCode(exercice.getCode());
    if (existingId != null && existingId.longValue() != exercice.getId().longValue()) {
      throw new EntityExistsException("The code for the exercise already exists");
    }
    Long idExerciceFetchedWithDateDebut = exerciceRepository.findExerciceIdByBetweenDateDebutAnAndDateFin(exercice.getDateDebut()).orElse(null);
    if (idExerciceFetchedWithDateDebut != null && idExerciceFetchedWithDateDebut.longValue() != exercice.getId().longValue()) {
      throw new
      EntityExistsException("La date de " +
          "début de l'exercice se trouve dans l'interval d'un autre exercice");
    }

    Long idExerciceFetchedWithDateFin = exerciceRepository.findExerciceIdByBetweenDateDebutAnAndDateFin(exercice.getDateFin()).orElse(null);
    if (idExerciceFetchedWithDateFin != null && idExerciceFetchedWithDateFin.longValue() != exercice.getId().longValue()) {
      throw new EntityExistsException("La date de fin de l'exercice se trouve dans l'interval d'un autre exercice");
    }

    Exercice exerciceToUpdate = exerciceRepository.findById(idExercice).orElseThrow(
        () -> new EntityNotFoundException("Exercise not found"));
    exerciceToUpdate = exercice;
    exerciceToUpdate = exerciceRepository.save(exerciceToUpdate);
    if (exerciceToUpdate.getId() != null) {
      String appEventLoggerJson = Helper.getJsonString(exerciceToUpdate);
      System.out.println("AppEventLogger in JSON format : " + appEventLoggerJson);
      appEventLogger.setEntityJson(appEventLoggerJson);
      appEventLoggerRepository.save(appEventLogger);
    }
    return exerciceToUpdate;
  }
}