package bi.gov.obr.informationServices.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import bi.gov.obr.informationServices.dto.RecetteObrForm;
import bi.gov.obr.informationServices.dto.ReportTwo;
import bi.gov.obr.informationServices.entity.RecetteObr;
import bi.gov.obr.informationServices.utils.RecetteObrDatatable;

public interface RecetteObrService {

  ReportTwo buildReportTwoSum(List<ReportTwo> reportTwoRecordList);
  List<RecetteObr> findAllByPeriod(LocalDate dateDebut, LocalDate dateFin);
  List<RecetteObr> findAllByPeriodAndTypeCodeRecette(LocalDate dateDebut, LocalDate dateFin, String typeCodeRecette);

  List<RecetteObr> findAllByPrevisionWithinPeriod(LocalDate dateDebut, LocalDate dateFin);

  RecetteObr findById(Long id);

  RecetteObr findByNumeroQuittance(String numeroQuittance);

  // RecetteObrDatatable generateReport(Long idAnneBudgetaire, Pageable pageable);

  Page<ReportTwo> findReportTwoRecords(List<String> posteCollectes, List<String> typesRecettes, List<String> devises,
      LocalDate startingDate,
      LocalDate endingDate, int pageIndex);

  RecetteObrDatatable generateReport(RecetteObrForm recetteObrForm);

  boolean loadRecetteObrFromExcel(MultipartFile file);
  boolean saveAll(List<RecetteObr> recetteObrs);
}
