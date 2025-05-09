package bi.gov.obr.informationServices.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import bi.gov.obr.informationServices.dto.ReportOneForm;
import bi.gov.obr.informationServices.dto.ReportOneProjection;
import bi.gov.obr.informationServices.dto.ReportTwoDTO;
import bi.gov.obr.informationServices.entity.RecetteObr;

public interface ReportService {
  ReportOneProjection buildTotalReportOne(List<ReportOneProjection> reportOneProjections);
  List<RecetteObr> findDailyRecordsUpToJMinusOne(List<String> centreCollectes, LocalDate d);

  Page<ReportOneProjection> findDailyRecordsUpToJMinusOneByPage(ReportOneForm reportOneForm, Pageable pageable);
  List<ReportOneProjection> findDailyRecordsUpToJMinusOneForExport(ReportOneForm reportOneForm);
  List<ReportOneProjection> findRecordsWithinPeriodForExport(ReportTwoDTO reportTwoDTO);

  Page<ReportOneProjection> getDailyRecordsWith4Params(List<String> centres, List<String> typesRecettes,
      List<String> devises, LocalDate d, Pageable pageable);

  Page<ReportOneProjection> findRecordsWithinPeriodByPage(ReportTwoDTO reportTwoDTO,
                                                          Pageable pageable);
}
