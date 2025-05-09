package bi.gov.obr.informationServices.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bi.gov.obr.informationServices.dto.RecetteObrForm;
import bi.gov.obr.informationServices.dto.ReportTwo;
import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.entity.Prevision;
import bi.gov.obr.informationServices.entity.RecetteObr;
import bi.gov.obr.informationServices.entity.RecetteRapport;
import bi.gov.obr.informationServices.repository.RecetteObrRepository;
import bi.gov.obr.informationServices.utils.RecetteObrDatatable;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecetteObrServiceImpl implements RecetteObrService {

  private final RecetteObrRepository recetteObrRepository;
  private final PrevisionService previsionService;
  private final ExerciceServiceImpl exerciceServiceImpl;
  private final CompteImpotService compteImpotService;
  @Autowired
  private RecetteObrRepository recetteRepo;

  private RecetteObrDatatable buildFinalResult(Exercice anneeBudgetaire) {
    List<RecetteObr> recetteObrs = recetteObrRepository
        .findRecetteObrByDatePaiementBetween(anneeBudgetaire.getDateDebut(), anneeBudgetaire.getDateFin());
    System.out.println(" recetteObrs " + recetteObrs.size());
    List<Prevision> allPrevisions = new ArrayList<>();
    List<CompteImpot> allCodeRecettes = compteImpotService.findAllCompteImpot();
    System.out.println(" ======> allCodeRecettes " + allCodeRecettes.size());
    // Include prevision
    allPrevisions = previsionService.loadPrevisionByAnneeBudgetaire(anneeBudgetaire.getId());
    System.out.println(" ===> allPrevisions " + allPrevisions.size());

    return buildRecetteDatatable(anneeBudgetaire, recetteObrs, allPrevisions, true, allCodeRecettes);
  }



  private RecetteObrDatatable buildRecetteDatatable(Exercice anneeBudgetaire, List<RecetteObr> recetteObrs,
      List<Prevision> allPrevisions, boolean inclurePrevision, List<CompteImpot> allCodeRecettes) {

    RecetteObrDatatable recetteDatatable = new RecetteObrDatatable();
    List<RecetteRapport> recetteRapports = new ArrayList<>();
    // Here I group prevision by codeRecette firstly then by Month
    Map<String, Map<Month, List<Prevision>>> previsionGrouped = allPrevisions.stream().collect(Collectors
        .groupingBy(prev -> prev.getCodeRecette().getCode(), Collectors.groupingBy(Prevision::getPrevisionMonth)));

    System.out.println(" prevision grouped  " + previsionGrouped.entrySet().size());
    previsionGrouped.entrySet().forEach(
        entr -> System.out.println(" ===> " + entr.getKey() + " and by month " + entr.getValue().entrySet().size()));

    System.out.println(" ================> ===============> <====================== <====================== ");
    Map<String, Map<Month, List<RecetteObr>>> recetteObrGrouped = recetteObrs.stream().collect(Collectors.groupingBy(
        RecetteObr::getCodeRecette, Collectors.groupingBy(recetteObr -> recetteObr.getDatePaiement().getMonth())));

    recetteObrGrouped.entrySet().forEach(
        entr -> System.out.println(" ===> " + entr.getKey() + " and by month " + entr.getValue().entrySet().size()));
    RecetteRapport recetteRapport;

    for (CompteImpot codeRecette : allCodeRecettes) {
      recetteRapport = new RecetteRapport();
      recetteRapport.setCodeRecette(codeRecette.getCode());
      recetteRapport.setLibelleRecette(codeRecette.getLibelle());
      // Search for the current item of codeRecete in the keys of previsionGrouped
      // Add in the result the codeRecette
      if (previsionGrouped.containsKey(codeRecette.getCode())) {

        Map<Month, List<Prevision>> entrySetPerMonth = previsionGrouped.get(codeRecette.getCode());
        extractMonthlyPrevision(entrySetPerMonth, recetteRapport);
      }

      // Search for the current item of codeRecete in the keys of recetteObrGrouped
      // Add in the result the codeRecette
      if (recetteObrGrouped.containsKey(codeRecette.getCode())) {

        Map<Month, List<RecetteObr>> entrySetPerMonth = recetteObrGrouped.get(codeRecette.getCode());
        extractMonthlyRecetteObr(entrySetPerMonth, recetteRapport);
      }

      // finaly adding recetteRapports to the list to display
      recetteRapports.add(recetteRapport);

    }

    // recetteRapports.forEach(System.out::println);
    recetteDatatable.setLength(10);
    recetteDatatable.setDraw(1);
    recetteDatatable.setRecettesData(recetteRapports);
    recetteDatatable.setRecordsTotal(recetteDatatable.getRecettesData().size());
    recetteDatatable.setRecetteRapportTotal(summarizeRecetteTotal(recetteRapports));
    return recetteDatatable;
  }

  @Override
  public ReportTwo buildReportTwoSum(List<ReportTwo> reportTwoRecordList) {
    ReportTwo reportTwo = new ReportTwo();
    reportTwo.setMontant(0L);
    reportTwoRecordList.forEach(record -> {
      Long montant = record.getMontant();
      reportTwo.setMontant(montant + reportTwo.getMontant());
    });

    return reportTwo;
  }

  // recetteRapport will get All prevision amount set
  private void extractMonthlyPrevision(Map<Month, List<Prevision>> entrySetPerMonth, RecetteRapport recetteRapport) {
  }

  // recetteRapport will get All recetteObr amount set
  private void extractMonthlyRecetteObr(Map<Month, List<RecetteObr>> entrySetPerMonth, RecetteRapport recetteRapport) {

    for (int m = 1; m < 13; m++) {
    }

  }

  @Override
  public List<RecetteObr> findAllByPeriod(LocalDate dateDebut, LocalDate dateFin) {
    return null;
  }

  @Override
  public List<RecetteObr> findAllByPeriodAndTypeCodeRecette(LocalDate dateDebut, LocalDate dateFin,
      String typeCodeRecette) {
    return null;
  }

  @Override
  public List<RecetteObr> findAllByPrevisionWithinPeriod(LocalDate dateDebut, LocalDate dateFin) {
    return null;
  }

  @Override
  public RecetteObr findById(Long id) {
    return null;
  }

  @Override
  public RecetteObr findByNumeroQuittance(String numeroQuittance) {
    return null;
  }

  /*
   * @Override public RecetteObrDatatable generateReport(Long idAnneBudgetaire,
   * Pageable pageable) {
   *
   * Exercice anneeBudgetaire = exerciceServiceImpl.findById(idAnneBudgetaire);
   * return buildFinalResultWithPagination(anneeBudgetaire, pageable); }
   */



  @Override
  public Page<ReportTwo> findReportTwoRecords(List<String> posteCollectes, List<String> typesRecettes,
      List<String> devises, LocalDate startingDate, LocalDate endingDate, int pageIndex) {
    // TODO Auto-generated method stub
    return null;
  }








  @Override
  public RecetteObrDatatable generateReport(RecetteObrForm recetteObrForm) {

    Exercice anneeBudgetaire = exerciceServiceImpl.findById(recetteObrForm.getSelectedAnneeBudgetaireId());

    return buildFinalResult(anneeBudgetaire);
  }

  @Override
  public boolean loadRecetteObrFromExcel(MultipartFile file) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean saveAll(List<RecetteObr> recetteObrs) {
    // TODO Auto-generated method stub
    return false;
  }

  // Take the list of Prevision and summarize the montant or montantRevisE
  // Probably this list has one item
  private BigDecimal summarizePrevisionAmount(List<Prevision> previsions) {
    BigDecimal montantPrev = BigDecimal.ZERO;
    for (Prevision prev : previsions) {
      if (prev.getMontantReviseCda() != null) {
        montantPrev = montantPrev.add(prev.getMontantReviseCda());
      } else if (prev.getMontantReviseCti() != null) {
        montantPrev = montantPrev.add(prev.getMontantReviseCti());
      }
      else {
        if(prev.getMontantCti() != null)
          montantPrev = montantPrev.add(prev.getMontantCti());
        else
        montantPrev = montantPrev.add(prev.getMontantCda());
      }
    }
    return montantPrev;
  }

  // Take the list of Prevision and summarize the montant or montantRevisE
  // Probably this list has one item
  private BigDecimal summarizeRecetteAmount(List<RecetteObr> recetteObrs) {
    BigDecimal montantPrev = BigDecimal.ZERO;
    if (recetteObrs != null) {
      for (RecetteObr recette : recetteObrs) {
        // if (recette.getMontantRecette() != null)
        // montantPrev = montantPrev.add(recette.getMontantRecette());
      }
    }
    return montantPrev;
  }

  private RecetteRapport summarizeRecetteTotal(List<RecetteRapport> recetteRapports) {

    RecetteRapport recetteRapport = new RecetteRapport();
    recetteRapports.forEach(rp -> {
      if (recetteRapport.getRecetteJan() == null) {
        recetteRapport.setRecetteJan(BigDecimal.ZERO);
      }
      if (rp.getRecetteJan() == null) {
        rp.setRecetteJan(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteJan(rp.getRecetteJan().add(recetteRapport.getRecetteJan()));

      if (recetteRapport.getRecetteFev() == null) {
        recetteRapport.setRecetteFev(BigDecimal.ZERO);
      }
      if (rp.getRecetteFev() == null) {
        rp.setRecetteFev(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteFev(rp.getRecetteFev().add(recetteRapport.getRecetteFev()));

      if (recetteRapport.getRecetteMar() == null) {
        recetteRapport.setRecetteMar(BigDecimal.ZERO);
      }
      if (rp.getRecetteMar() == null) {
        rp.setRecetteMar(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteMar(rp.getRecetteMar().add(recetteRapport.getRecetteMar()));

      if (recetteRapport.getRecetteApr() == null) {
        recetteRapport.setRecetteApr((BigDecimal.ZERO));
      }
      if (rp.getRecetteApr() == null) {
        rp.setRecetteApr(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteApr(rp.getRecetteApr().add(recetteRapport.getRecetteApr()));

      if (recetteRapport.getRecetteMay() == null) {
        recetteRapport.setRecetteMay((BigDecimal.ZERO));
      }
      if (rp.getRecetteMay() == null) {
        rp.setRecetteMay(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteMay(rp.getRecetteMay().add(recetteRapport.getRecetteMay()));

      if (recetteRapport.getRecetteJun() == null) {
        recetteRapport.setRecetteJun(BigDecimal.ZERO);
      }
      if (rp.getRecetteJun() == null) {
        rp.setRecetteJun(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteJun(rp.getRecetteJun().add(recetteRapport.getRecetteJun()));

      if (recetteRapport.getRecetteJul() == null) {
        recetteRapport.setRecetteJul(BigDecimal.ZERO);
      }
      if (rp.getRecetteJul() == null) {
        rp.setRecetteJul(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteJul(rp.getRecetteJul().add(recetteRapport.getRecetteJul()));

      if (recetteRapport.getRecetteAug() == null) {
        recetteRapport.setRecetteAug(BigDecimal.ZERO);
      }
      if (rp.getRecetteAug() == null) {
        rp.setRecetteAug(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteAug(rp.getRecetteAug().add(recetteRapport.getRecetteAug()));

      if (recetteRapport.getRecetteSep() == null) {
        recetteRapport.setRecetteSep(BigDecimal.ZERO);
      }
      if (rp.getRecetteSep() == null) {
        rp.setRecetteSep(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteSep(rp.getRecetteSep().add(recetteRapport.getRecetteSep()));

      if (recetteRapport.getRecetteOct() == null) {
        recetteRapport.setRecetteOct(BigDecimal.ZERO);
      }
      if (rp.getRecetteOct() == null) {
        rp.setRecetteOct(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteOct(rp.getRecetteOct().add(recetteRapport.getRecetteOct()));

      if (recetteRapport.getRecetteNov() == null) {
        recetteRapport.setRecetteNov(BigDecimal.ZERO);
      }
      if (rp.getRecetteNov() == null) {
        rp.setRecetteNov(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteNov(rp.getRecetteNov().add(recetteRapport.getRecetteNov()));

      if (recetteRapport.getRecetteDec() == null) {
        recetteRapport.setRecetteDec(BigDecimal.ZERO);
      }
      if (rp.getRecetteDec() == null) {
        rp.setRecetteDec(BigDecimal.ZERO);
      }
      recetteRapport.setRecetteDec(rp.getRecetteDec().add(recetteRapport.getRecetteDec()));

    });
    return recetteRapport;
  }
}