package bi.gov.obr.informationServices.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.dto.SituationConsolideeResponse;
import bi.gov.obr.informationServices.repository.RecetteObrRepository;

@Service
public class SituaConsolideeServiceImpl implements SituaConsolideeService {
  @Autowired
  private RecetteObrRepository recetteRepo;
  @Override
  public List<SituationConsolideeResponse> findSituationConsolidee(Long exerciceId, LocalDate dateDebutEnCours,
      LocalDate dateFinEnCours, LocalDate dateDebutAnPrec, LocalDate dateFinAnPrec, int MonthPrevisionId) {

    List<SituationConsolideeResponse> situationConsolideeResponses = recetteRepo.findSituationConsolidee(exerciceId,
        dateDebutEnCours, dateFinEnCours, dateDebutAnPrec, dateFinAnPrec, MonthPrevisionId);

    return situationConsolideeResponses;
  }

}
