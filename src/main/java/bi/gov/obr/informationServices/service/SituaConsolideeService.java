package bi.gov.obr.informationServices.service;

import java.time.LocalDate;
import java.util.List;

import bi.gov.obr.informationServices.dto.SituationConsolideeResponse;

public interface SituaConsolideeService {


  List<SituationConsolideeResponse> findSituationConsolidee(Long exerciceId, LocalDate dateDebutEnCours,
      LocalDate dateFinEnCours, LocalDate dateDebutAnPrec, LocalDate dateFinAnPrec, int monthPrevisionId);
}
