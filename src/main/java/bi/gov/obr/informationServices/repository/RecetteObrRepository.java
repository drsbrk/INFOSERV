package bi.gov.obr.informationServices.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bi.gov.obr.informationServices.dto.ReportOneProjection;
import bi.gov.obr.informationServices.dto.ReportTwo;
import bi.gov.obr.informationServices.dto.SituationConsolideeResponse;
import bi.gov.obr.informationServices.entity.RecetteObr;

public interface RecetteObrRepository extends JpaRepository<RecetteObr, Long> {

  List<RecetteObr> findAllByNumeroQuittanceIn(List<String> numeroQuittances);

  @Query(name ="ReportOne", countName = "ReportOne.count", nativeQuery = true)
  Page<ReportOneProjection> findDailyRecords(LocalDate debutMois, LocalDate jMoinsUn, LocalDate jMoinsDeux,
      List<String> centreCollectes, Pageable pageable);

  @Query(name = "ReportOneWhenCentreCollecteIsEmpty", nativeQuery = true)
  List<ReportOneProjection> findDailyRecordsWithEmptyCentreCollecte(LocalDate debutMois, LocalDate jMoinsUn,
      LocalDate jMoinsDeux,List<String> posteCollecteCodes,List<String> codeRecettes,List<String> devises, Integer offSet, Integer limit);

  List<RecetteObr> findRecetteObrByDatePaiementBetween(LocalDate debutPeriode, LocalDate finPeriode);

  @Query(name = "ReportRecordWithinPeriodWhenCentreCollecteIsEmpty", nativeQuery = true)
  List<ReportOneProjection> findRecordsWithinPeriodWithEmptyCentreCollecte(LocalDate startingPeriod, LocalDate endingPeriod,
      List<String> posteCollecteCodes,List<String> typeRecettes,List<String> devises, Integer offSet, Integer limit);

  @Query(name="SituationConsolide.find", nativeQuery=true)
  List<SituationConsolideeResponse> findSituationConsolidee(Long exerciceId, LocalDate dateDebutEnCours,
      LocalDate dateFinEnCours, LocalDate dateDebutAnPrec, LocalDate dateFinAnPrec, int monthPrevisionId);

  @Query(name = "RecetteObr.ReportOneWith4Params", nativeQuery = true)
  Page<ReportOneProjection> getReportOneWith4Params(List<String> centres, List<String> typesRecettes,
      List<String> devises, LocalDate debutMois, LocalDate jMoins1, LocalDate jMoins2, Pageable pageable);

  @Query(name = "RecetteObr.getReportTwoRecords", nativeQuery = true)
  Page<ReportTwo> getReportTwoRecords(List<String> centresDeCollecte, List<String> codeRecette, List<String> devises,
      LocalDate startingDate, LocalDate endingDate, Pageable pageable);


}
