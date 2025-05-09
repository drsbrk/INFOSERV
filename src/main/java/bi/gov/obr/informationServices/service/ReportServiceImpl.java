package bi.gov.obr.informationServices.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.dto.ReportOneForm;
import bi.gov.obr.informationServices.dto.ReportOneProjection;
import bi.gov.obr.informationServices.dto.ReportTwoDTO;
import bi.gov.obr.informationServices.entity.RecetteObr;
import bi.gov.obr.informationServices.enums.Devise;
import bi.gov.obr.informationServices.repository.CompteImpotRepository;
import bi.gov.obr.informationServices.repository.PosteCollecteRepository;
import bi.gov.obr.informationServices.repository.RecetteObrRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final RecetteObrRepository recetteRepo;
  private final PosteCollecteRepository posteCollecteRepository;
  private final CompteImpotRepository compteImpotRepository;

  @Override
  public ReportOneProjection buildTotalReportOne(List<ReportOneProjection> reportOneProjections) {

    ReportOneProjection reportOneProjection = new ReportOneProjection();
    reportOneProjection.setMontantRecetteJMoinsDeux(0L);
    reportOneProjection.setMontantRecetteJMoinsUn(0L);
    reportOneProjections.forEach(rep ->
    {
      reportOneProjection.setMontantRecetteJMoinsUn(rep.getMontantRecetteJMoinsUn() + reportOneProjection.getMontantRecetteJMoinsUn());
      reportOneProjection.setMontantRecetteJMoinsDeux(rep.getMontantRecetteJMoinsDeux() + reportOneProjection.getMontantRecetteJMoinsDeux());
    });


    return reportOneProjection;
  }

  @Override
  public List<RecetteObr> findDailyRecordsUpToJMinusOne(List<String> centreCollectes, LocalDate d) {
    return Collections.emptyList();
  }


  @Override
  public Page<ReportOneProjection> findDailyRecordsUpToJMinusOneByPage(ReportOneForm reportOneForm,
      Pageable pageable) {

    LocalDate jMoinsDeux = reportOneForm.getSelectedDate().minusDays(2);
    LocalDate jMoinsUn = reportOneForm.getSelectedDate().minusDays(1);
    LocalDate dateDebutMois = reportOneForm.getSelectedDate().withDayOfMonth(1); // Day one of month
    List<String> codeRecettesSelected = compteImpotRepository.findAllByCodeByType(reportOneForm.getTypeRecettesSelected());
    reportOneForm.setCodeRecettesSelected(codeRecettesSelected);

    List<String> devises = new ArrayList<>();
    reportOneForm.getDevisesSelected().forEach(dev -> {
      devises.add(Devise.getDevise(dev).getLibelle());
    });
    //Replace the choosen currency by its symbol
    reportOneForm.setDevisesSelected(devises);

    reportOneForm.getDevisesSelected().forEach(dev -> {
      System.out.println(" ===> " + dev);
    });
    if (reportOneForm.getCentreCollectesSelected() != null) {
      System.out.println(" 1 not null");
    }

    if (reportOneForm.getCodeRecettesSelected() != null) {
      System.out.println(" 2 not null");
    }

    if (reportOneForm.getDevisesSelected() != null) {
      System.out.println(" 3 not null");
    }

    if (reportOneForm.getSelectedDate() != null) {
      System.out.println(" 4 not null");
    }

    if (reportOneForm.getTypeRecettesSelected() != null) {
      System.out.println(" 6 not null");
    }


    List<ReportOneProjection> reportOneProjections = recetteRepo.findDailyRecordsWithEmptyCentreCollecte(
        dateDebutMois,
        jMoinsUn,
        jMoinsDeux,
        reportOneForm.getCentreCollectesSelected(),
        reportOneForm.getTypeRecettesSelected(),
        reportOneForm.getDevisesSelected(),
        pageable.getPageNumber() * pageable.getPageSize(),
        pageable.getPageSize());
    List<Long> allPostes = posteCollecteRepository.getAllPosteCollecteWithRecetteObr(dateDebutMois, jMoinsUn,
        reportOneForm.getCentreCollectesSelected(), reportOneForm.getTypeRecettesSelected(),
        reportOneForm.getDevisesSelected());


    return new PageImpl<>(reportOneProjections, pageable, allPostes.size());


  }

  @Override
  public List<ReportOneProjection> findDailyRecordsUpToJMinusOneForExport(ReportOneForm reportOneForm) {

    LocalDate jMoinsDeux = reportOneForm.getSelectedDate().minusDays(2);
    LocalDate jMoinsUn = reportOneForm.getSelectedDate().minusDays(1);
    LocalDate dateDebutMois = reportOneForm.getSelectedDate().withDayOfMonth(1); // Day one of month

    List<ReportOneProjection> reportOneProjections = recetteRepo.findDailyRecordsWithEmptyCentreCollecte(
        dateDebutMois,
        jMoinsUn,
        jMoinsDeux,
        reportOneForm.getCentreCollectesSelected(),
        reportOneForm.getCodeRecettesSelected(),
        reportOneForm.getDevisesSelected(),
        0,
        Integer.MAX_VALUE);
    return reportOneProjections;
  }

  @Override
  public Page<ReportOneProjection> findRecordsWithinPeriodByPage(ReportTwoDTO reportTwoDTO,
      Pageable pageable) {


    List<String> devises = new ArrayList<>();
    reportTwoDTO.getDevises().forEach(dev -> {
      devises.add(Devise.getDevise(dev.getCode()).getLibelle());
    });
    //Replace the choosen currency by its symbol
    List<String> deviseSelectedLibelle = new ArrayList<>();

    if (reportTwoDTO.getDevisesSelected() != null) {
      reportTwoDTO.getDevisesSelected().forEach(dev -> {
        deviseSelectedLibelle.add(Devise.getDevise(dev).getLibelle());
      });
      reportTwoDTO.setDevisesSelected(deviseSelectedLibelle);
    }


    System.out.println(" les types de recettes sont " + reportTwoDTO.getSelectedTypeRecette().size());

    System.out.println("after les types de recettes sont " + reportTwoDTO.getSelectedTypeRecette().size());


    if (reportTwoDTO.getCentreDeCollectesSelected() != null) {
      System.out.println(" 1 not null size " + reportTwoDTO.getCentreDeCollectesSelected().size() );
    }

    reportTwoDTO.getCentreDeCollectesSelected().forEach(cet -> {
      System.out.println("Centre cet " + cet);
    });


    List<ReportOneProjection> reportOneProjections = recetteRepo.findRecordsWithinPeriodWithEmptyCentreCollecte(
        reportTwoDTO.getStartingDate(),
        reportTwoDTO.getEndingDate(),
        reportTwoDTO.getCentreDeCollectesSelected(),
        reportTwoDTO.getSelectedTypeRecette(),
        reportTwoDTO.getDevisesSelected(),
        pageable.getPageNumber() * pageable.getPageSize(),
        pageable.getPageSize());
    System.out.println(" First thing first ===> " + reportOneProjections.size());
    List<Long> allPostes = posteCollecteRepository.getAllPosteCollecteWithTypeRecetteObr(reportTwoDTO.getStartingDate(), reportTwoDTO.getEndingDate(), reportTwoDTO.getCentreDeCollectesSelected(), reportTwoDTO.getSelectedTypeRecette(), reportTwoDTO.getDevisesSelected());
    System.out.println(" Secondly ===> " + allPostes.size());
    return new PageImpl<>(reportOneProjections, pageable, allPostes.size());
  }

  @Override
  public List<ReportOneProjection> findRecordsWithinPeriodForExport(ReportTwoDTO reportTwoDTO) {

    List<ReportOneProjection> reportOneProjections = recetteRepo.findRecordsWithinPeriodWithEmptyCentreCollecte(
        reportTwoDTO.getStartingDate(),
        reportTwoDTO.getEndingDate(),
        reportTwoDTO.getCentreDeCollectesSelected(),
        reportTwoDTO.getCodeRecettesSelected(),
        reportTwoDTO.getDevisesSelected(),
        0,
        Integer.MAX_VALUE);
    return reportOneProjections;
  }

  @Override
  public Page<ReportOneProjection> getDailyRecordsWith4Params(List<String> centres, List<String> typesRecettes,
      List<String> devises, LocalDate d, Pageable pageable) {

    LocalDate jMoinsDeux = d.minusDays(2);
    LocalDate jMoinsUn = d.minusDays(1);
    LocalDate dateDebutMois = d.withDayOfMonth(1); // Day one of month

    if (centres != null && typesRecettes != null && devises != null) {
      return recetteRepo.getReportOneWith4Params(centres, typesRecettes, devises, dateDebutMois, jMoinsUn, jMoinsDeux,
          pageable);
    } else {
      //            return recetteRepo.getReportOneWithListEmpty(dateDebutMois, jMoinsUn, jMoinsDeux,
      //                    pageable);
      return null;
    }
  }
}
