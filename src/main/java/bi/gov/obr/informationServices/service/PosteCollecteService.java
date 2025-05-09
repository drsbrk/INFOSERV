package bi.gov.obr.informationServices.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import bi.gov.obr.informationServices.entity.AppEventLogger;
import bi.gov.obr.informationServices.entity.PosteCollecte;
import bi.gov.obr.informationServices.utils.Paged;

public interface PosteCollecteService {

  boolean deletePosteCollecte(Long idPosteCollecte);

  List<PosteCollecte> findAllPosteCollecte();
  Paged<PosteCollecte> findAllPosteCollecte(Pageable pageable);

  PosteCollecte findByCode(String idPosteCollecte);

  List<PosteCollecte> findPoste();

  PosteCollecte savePosteCollecte(PosteCollecte posteCollecte, AppEventLogger appEventLogger);

  PosteCollecte updatePosteCollecte(String idPosteCollcte, PosteCollecte posteCollecte, AppEventLogger appEventLogger);
}
