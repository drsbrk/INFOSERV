package bi.gov.obr.informationServices.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.entity.AppEventLogger;
import bi.gov.obr.informationServices.entity.PosteCollecte;
import bi.gov.obr.informationServices.repository.AppEventLoggerRepository;
import bi.gov.obr.informationServices.repository.PosteCollecteRepository;
import bi.gov.obr.informationServices.utils.Helper;
import bi.gov.obr.informationServices.utils.Paged;
import bi.gov.obr.informationServices.utils.Paging;

@Service
public class PosteCollecteServiceImpl implements PosteCollecteService {

  private final PosteCollecteRepository posteCollecteRepository;
  private final AppEventLoggerRepository appEventLoggerRepository;

  public PosteCollecteServiceImpl(PosteCollecteRepository posteCollecteRepository, AppEventLoggerRepository appEventLoggerRepository) {

    this.posteCollecteRepository = posteCollecteRepository;
    this.appEventLoggerRepository = appEventLoggerRepository;
  }

  @Override
  public boolean deletePosteCollecte(Long idPosteCollecte) {
    posteCollecteRepository.deleteById(idPosteCollecte);
    return true;
  }

  @Override
  public List<PosteCollecte> findAllPosteCollecte() {
    List<PosteCollecte> posteCollectes = posteCollecteRepository.findAllByOrderByCodeDesc();
    return posteCollectes;
  }

  @Override
  public Paged<PosteCollecte> findAllPosteCollecte(Pageable pageable) {

    Page<PosteCollecte> posteCollectePage = posteCollecteRepository.findAllByOrderByCodeDesc(pageable);
    return new Paged<>(posteCollectePage,
        Paging.of(posteCollectePage.getTotalPages(),
            pageable.getPageNumber(), pageable.getPageSize()));

  }

  @Override
  public PosteCollecte findByCode(String idPosteCollecte) {
    return posteCollecteRepository.findByCode(idPosteCollecte)
        .orElseThrow(
            () -> new EntityNotFoundException("Poste de collecte non trouvé"));
  }

  @Override
  public List<PosteCollecte> findPoste() {
    return posteCollecteRepository.findPosteUsingSet();
  }

  @Override
  public PosteCollecte savePosteCollecte(PosteCollecte posteCollecte, AppEventLogger appEventLogger) {

    Optional<PosteCollecte> posteCollecteFromDb = posteCollecteRepository.findByCode(posteCollecte.getCode());
    if (posteCollecteFromDb.isPresent()) {
      throw new EntityExistsException("Le code du poste de collecte existe déjà");
    }

    posteCollecte = posteCollecteRepository.save(posteCollecte);

    if (posteCollecte.getCode() != null) {
      String appEventLoggerJson = Helper.getJsonString(posteCollecte);
      System.out.println("AppEventLogger in JSON format : " + appEventLoggerJson);
      appEventLogger.setEntityJson(appEventLoggerJson);
      appEventLoggerRepository.save(appEventLogger);
    }
    return posteCollecte;
  }

  @Override
  @Modifying(clearAutomatically = true)
  public PosteCollecte updatePosteCollecte(String idPosteCollecte, PosteCollecte posteCollecte,
      AppEventLogger appEventLogger) {

    if (idPosteCollecte == null || posteCollecte.getCode() == null) {
      throw new IllegalArgumentException("L'identifiant du poste de collecte est null");
    }
    if (!idPosteCollecte.equals(posteCollecte.getCode())) {
      throw new IllegalArgumentException("L'identifiant du poste de collecte ne correspond pas à l'identifiant de l'objet poste de collecte");
    }

    String existingId = posteCollecteRepository.findByCode(posteCollecte.getCode()).get().getCode();
    if (existingId != null && existingId.equals(posteCollecte.getCode())) {
      throw new EntityExistsException("Le code du poste de collecte existe déjà");
    }

    PosteCollecte posteCollecteToUpdate = posteCollecteRepository.findByCode(idPosteCollecte).orElseThrow(
        () -> new EntityNotFoundException("Poste de collecte non trouvé"));
    posteCollecteToUpdate = posteCollecte;
    posteCollecteToUpdate = posteCollecteRepository.save(posteCollecteToUpdate);
    if (posteCollecteToUpdate.getCode() != null) {
      String appEventLoggerJson = Helper.getJsonString(posteCollecteToUpdate);
      System.out.println("AppEventLogger in JSON format : " + appEventLoggerJson);
      appEventLogger.setEntityJson(appEventLoggerJson);
      appEventLoggerRepository.save(appEventLogger);
    }
    return posteCollecteToUpdate;
  }
}
