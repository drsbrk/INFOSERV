package bi.gov.obr.informationServices.service;

import org.springframework.data.domain.Pageable;

import bi.gov.obr.informationServices.entity.AppEventLogger;
import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.utils.Paged;

import java.time.LocalDate;
import java.util.List;

public interface ExerciceService {

    Exercice saveExercice(Exercice exercice, AppEventLogger appEventLogger);

    Exercice findCurrentAnneeBudgetaire();

    List<Exercice> findAllExercice();
    Exercice findById(Long idExercice);

    Exercice updateExercice(Long idExercice, Exercice exercice, AppEventLogger appEventLogger);

    boolean deleteExercice(Long idExercice);

    Paged<Exercice> findAllExercice(Pageable pageable);

    Exercice findExerciceByDateBetween(LocalDate dateBetween);

}