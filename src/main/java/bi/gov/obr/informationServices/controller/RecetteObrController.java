package bi.gov.obr.informationServices.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.JsonObject;

import bi.gov.obr.informationServices.dto.ReportFourForm;
import bi.gov.obr.informationServices.dto.ReportOneForm;
import bi.gov.obr.informationServices.dto.ReportOneProjection;
import bi.gov.obr.informationServices.dto.ReportThreeDTO;
import bi.gov.obr.informationServices.dto.ReportTwoDTO;
import bi.gov.obr.informationServices.dto.SituationConsolideeResponse;
import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.entity.PosteCollecte;
import bi.gov.obr.informationServices.enums.Devise;
import bi.gov.obr.informationServices.enums.TypeCodeRecette;
import bi.gov.obr.informationServices.service.CompteImpotService;
import bi.gov.obr.informationServices.service.CompteImpotServiceImpl;
import bi.gov.obr.informationServices.service.ExerciceService;
import bi.gov.obr.informationServices.service.PosteCollecteService;
import bi.gov.obr.informationServices.service.RecetteObrService;
import bi.gov.obr.informationServices.service.ReportService;
import bi.gov.obr.informationServices.service.SituaConsolideeService;
import bi.gov.obr.informationServices.utils.MonthAnneeBudgetaire;
import bi.gov.obr.informationServices.utils.ReportOneExcelExporter;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@SessionAttributes({ "reportOneFormRequested", "reportTwoFormRequested" })
public class RecetteObrController {

  /*
   * @Autowired private HttpSession session;
   */

  private final RecetteObrService recetteObrService;
  private final ReportService reportService;
  private final ExerciceService anneeBudgetaireService;
  @Autowired
  private PosteCollecteService posteService;
  @Autowired
  private RecetteObrService recetteService;
  @Autowired
  private CompteImpotServiceImpl compteImpotServiceImpl;

  @Autowired
  private ExerciceService exerciceService;
  @Autowired
  private CompteImpotService compteImpotService;
  @Autowired
  private SituaConsolideeService situationConsolidee;

  public void addPosteCollecteIntoTheModel(Model model) {
    List<PosteCollecte> posteCollectes = posteService.findAllPosteCollecte();
    List<Exercice> anneesBudgetaires = anneeBudgetaireService.findAllExercice();
    Exercice currentAnnEeBudgetaire = anneeBudgetaireService.findCurrentAnneeBudgetaire();
    List<CompteImpot> codeRecettes = compteImpotServiceImpl.findAllCompteImpot();

    model.addAttribute("centreCollectes", posteCollectes);
    model.addAttribute("codeRecettes", codeRecettes);
    model.addAttribute("anneesBudgetaires", anneesBudgetaires);

    model.addAttribute("moisAnneeBudgetaire", buildMonthWithAnneeBudgetaire(currentAnnEeBudgetaire));
  }

  private List<MonthAnneeBudgetaire> buildMonthWithAnneeBudgetaire(Exercice exercice) {
    List<MonthAnneeBudgetaire> monthAnneeBudgetaires = new ArrayList<>();
    for (int m = 0; m < 12; m++) {
      Month currentMonth = exercice.getDateDebut().plusMonths(m).getMonth();
      MonthAnneeBudgetaire monthAnneeBudgetaire = new MonthAnneeBudgetaire();
      monthAnneeBudgetaire.setAnneE(exercice.getDateDebut().plusMonths(m).getYear());
      LocalDate debut = LocalDate.of(monthAnneeBudgetaire.getAnneE(), currentMonth, 1);
      LocalDate fin = LocalDate.of(monthAnneeBudgetaire.getAnneE(), currentMonth, 1)
          .withDayOfMonth(currentMonth.length(debut.isLeapYear()));
      monthAnneeBudgetaire.setMonthId(debut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "|"
          + fin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      monthAnneeBudgetaire
      .setMoisAnneeBudgetaire(StringUtils.capitalize(currentMonth.getDisplayName(TextStyle.FULL, Locale.FRANCE))
          + " " + monthAnneeBudgetaire.getAnneE());
      monthAnneeBudgetaire.setMois(StringUtils.capitalize(currentMonth.getDisplayName(TextStyle.FULL, Locale.FRANCE)));
      monthAnneeBudgetaires.add(monthAnneeBudgetaire);
    }
    return monthAnneeBudgetaires;
  }

  @GetMapping("/collecte")
  public String buildRecetteObrView(HttpServletRequest request, Model model,
      @RequestParam(name = "page", defaultValue = "0", required = false) int page,
      @RequestParam(name = "size", defaultValue = "5", required = false) int size) {
    List<PosteCollecte> posteCollectes = posteService.findPoste();
    List<Exercice> exercices = exerciceService.findAllExercice();
    List<CompteImpot> compteImpots = compteImpotService.findAllCompteImpot();
    Pageable pageable = PageRequest.of(page, size);

    // Prepare list of string code postes de collecte
    List<String> centresDeCollecte = new ArrayList<>();
    posteCollectes.forEach(p -> {
      centresDeCollecte.add(p.getCode());
    });
    Page<ReportOneProjection> reportOneDtos = new PageImpl<>(new ArrayList<ReportOneProjection>(), pageable, 0);

    ReportOneForm reportOneForm = new ReportOneForm();
    ReportTwoDTO reportTwoDTO = new ReportTwoDTO();
    ReportThreeDTO reportThreeDTO = new ReportThreeDTO();
    ReportFourForm reportFourForm = new ReportFourForm();

    for (int i = 1; i < TypeCodeRecette.values().length; i++) {
      reportTwoDTO.getTypesRecettes().add(TypeCodeRecette.values()[i]);
      reportOneForm.getTypesRecettes().add(TypeCodeRecette.values()[i]);
      reportThreeDTO.getTypesRecettes().add(TypeCodeRecette.values()[i]);
    }

    for (int j = 1; j < Devise.values().length; j++) {
      reportTwoDTO.getDevises().add(Devise.values()[j]);
      reportOneForm.getDevises().add(Devise.values()[j]);
      reportThreeDTO.getDevises().add(Devise.values()[j]);
    }

    reportOneForm.setCentreCollectes(posteCollectes);
    reportTwoDTO.setCentreDeCollectes(posteCollectes);
    reportThreeDTO.setCentreDeCollectes(posteCollectes);
    reportThreeDTO.setCompteImpot(compteImpots);
    reportThreeDTO.setExercices(exercices);

    // Building reportFourForm

    reportFourForm.setExercices(exercices);

    reportFourForm.getMonthAnneeBudgetaires().forEach(m -> {
      System.out.println(" Mois " + m.getMoisAnneeBudgetaire());
    });

    HttpSession session = request.getSession();
    String navigation = (String) session.getAttribute("navMenu");

    model.addAttribute("dailyReportPage", reportOneDtos);
    model.addAttribute("dailyReportPageSize", reportOneDtos.getPageable().getPageSize());
    model.addAttribute("dailyReportTotalElements", reportOneDtos.getTotalElements());
    model.addAttribute("dailyReportPageTotalPages", new int[reportOneDtos.getTotalPages()]);
    model.addAttribute("dailyReportPageCurrentPage", reportOneDtos.getNumber() + 1);
    model.addAttribute("dailyReportTotalAmount", reportService.buildTotalReportOne(reportOneDtos.getContent()));
    model.addAttribute("currentPage", page);
    model.addAttribute("size", size);
    model.addAttribute("reportOneForm", reportOneForm);
    model.addAttribute("reportTwoDTO", reportTwoDTO);
    model.addAttribute("reportThreeDTO", reportThreeDTO);
    model.addAttribute("reportFourDTO", reportFourForm);
    model.addAttribute("keyword", "");
    model.addAttribute("navMenu", navigation);
    model.addAttribute("exercices", exercices);
    model.addAttribute("moisAnneeBudgetaires",
        buildMonthWithAnneeBudgetaire(exerciceService.findCurrentAnneeBudgetaire()));

    if (navigation == null || navigation.isEmpty()) {
      return "redirect:/index";
    }
    return "recetteObr";
  }

  @PostMapping(value = "/reportOne")
  public String displayRecetteRecordsForReportOne(@RequestBody ReportOneForm reportOneForm, Model model) {
    Pageable pageable = PageRequest.of(reportOneForm.getPage() - 1, reportOneForm.getSize());
    // reportOneDtos contains 5 records from DB

    Page<ReportOneProjection> reportOneDtos = reportService.findDailyRecordsUpToJMinusOneByPage(reportOneForm,
        pageable);
    model.addAttribute("dailyReportPage", reportOneDtos);
    model.addAttribute("dailyReportPageSize", reportOneDtos.getPageable().getPageSize());
    model.addAttribute("dailyReportTotalElements", reportOneDtos.getTotalElements());
    model.addAttribute("dailyReportPageTotalPages", reportOneDtos.getTotalPages());
    model.addAttribute("dailyReportPageCurrentPage", reportOneForm.getPage());
    model.addAttribute("dailyReportTotalAmount", reportService.buildTotalReportOne(reportOneDtos.getContent()));
    System.out.println(" ====> dailyReportPageTotalPages " + reportOneDtos.getTotalPages());
    System.out.println(" ====> dailyReportPageCurrentPage " + reportOneDtos.getNumber());
    System.out.println(" ====> dailyReportPageCurrentPage " + reportOneDtos.getNumber());
    model.addAttribute("reportOneFormRequested", reportOneForm);
    return "layout/rapport1::dailyReport_1";
  }

  @PostMapping(value = "/reportTwo")
  public String displayRecetteRecordsForReportTwo(@RequestBody ReportTwoDTO reportTwoDto, Model model) {
    Pageable pageable = PageRequest.of(reportTwoDto.getPageIndex() - 1, reportTwoDto.getSize());

    Page<ReportOneProjection> reportTwoDtosPage = reportService.findRecordsWithinPeriodByPage(reportTwoDto, pageable);

    model.addAttribute("dailyReportPage", reportTwoDtosPage);
    model.addAttribute("dailyReportPageSize", reportTwoDtosPage.getPageable().getPageSize());
    model.addAttribute("dailyReportTotalElements", reportTwoDtosPage.getTotalElements());
    model.addAttribute("dailyReportPageTotalPages", reportTwoDtosPage.getTotalPages());
    model.addAttribute("dailyReportPageCurrentPage", reportTwoDtosPage.getPageable().getPageNumber() + 1);

    System.out.println(" ====> dailyReportPageTotalPages " + reportTwoDtosPage.getTotalPages());
    System.out.println(" ====> dailyReportTotalElements " + reportTwoDtosPage.getTotalElements());
    System.out.println(" ====> dailyReportPageCurrentPage " + reportTwoDtosPage.getNumber());
    model.addAttribute("reportTwoFormRequested", reportTwoDto);

    return "layout/rapport2::incomesReports";

  }

  @PostMapping(value = "/reportFour")
  public String displayRecetteSummarizedReport(@RequestBody ReportFourForm reportFourForm, Model model) {

    System.out.println(" Annee budget selectionnE " + reportFourForm.getAnneeBudgetaireSelected());
    System.out.println(" Mois selectionnE " + reportFourForm.getMonthAnneeBudgetaireSelected());
    Long idExercice = reportFourForm.getAnneeBudgetaireSelected();
    LocalDate monthStartingDate = LocalDate.parse(reportFourForm.getMonthAnneeBudgetaireSelected().split("\\|")[0],
        DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    LocalDate monthEndingDate = LocalDate.parse(reportFourForm.getMonthAnneeBudgetaireSelected().split("\\|")[1],
        DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    LocalDate monthStartingDatePrevYear = monthStartingDate.minusYears(1);
    LocalDate monthEndingDatePrevYear = null;
    if (monthStartingDate.minusYears(1).getMonthValue() == 2 && monthStartingDate.minusYears(1).isLeapYear()) {
      monthEndingDatePrevYear = monthEndingDate.minusYears(1).plusDays(1);
    } else {
      monthEndingDatePrevYear = monthEndingDate.minusYears(1);
    }
    int previsionMonthId = monthStartingDate.getMonth().getValue() - 1;

    System.out.println(" previsionMonthId " + previsionMonthId);
    List<SituationConsolideeResponse> responses = situationConsolidee.findSituationConsolidee(idExercice,
        monthStartingDate, monthEndingDate, monthStartingDatePrevYear, monthEndingDatePrevYear, previsionMonthId);
    model.addAttribute("dailyReportElements", responses.size());
    responses.forEach(rep -> {
      System.out.println("Rep  ===>>>>> " + rep.getPrevision_Mensuelle_CTI() + " == "
          + rep.getRealisation_Mensuelle_CTI() + " === " + rep.getPourcentage_Realisation_Mensuelle_CTI());
    });

    responses.forEach(response -> { // CTI DATA
      model.addAttribute("cti_realisation", response.getRealisation_Mensuelle_CTI());
      model.addAttribute("cti_pre", response.getPrevision_Mensuelle_CTI());
      model.addAttribute("cti_ecart_mens", response.getEcart_Mensuel_Realisation_Prevision_CTI());
      model.addAttribute("cti_pourcentage", response.getPourcentage_Realisation_Mensuelle_CTI());
      model.addAttribute("cti_croissance", response.getCroissance_CTI());
      model.addAttribute("realisation_mensuelle_anprec_CTI", response.getRealisation_Mensuelle_Anprec_CTI());

      // CDA DATA

      model.addAttribute("realisation_mensuelle_CDA", response.getRealisation_Mensuelle_CDA());


      model.addAttribute("prevision_mensuelle_CDA", response.getPrevision_Mensuelle_CDA());
      model.addAttribute("ecart_mensuel_realisation_prevision_CDA",
          response.getEcart_Mensuel_Realisation_Prevision_CDA());
      System.out.println("Ecart mensuel CDA " + response.getEcart_Mensuel_Realisation_Prevision_CDA());
      System.out.println("realisation_mensuelle_CDA " + response.getRealisation_Mensuelle_CDA());
      System.out.println("prevision_mensuelle_CDA " + response.getPrevision_Mensuelle_CDA());
      model.addAttribute("pourcentage_realisation_mensuelle_CDA", response.getPourcentage_Realisation_Mensuelle_CDA());
      model.addAttribute("croissance_CDA", response.getCroissance_CDA());
      model.addAttribute("realisation_mensuelle_anprec_CDA", response.getRealisation_Mensuelle_Anprec_CDA());

      // TOTAUX DATA
      model.addAttribute("realisation_mensuelle_totale", response.getRealisation_Mensuelle_Totale());
      model.addAttribute("prevision_mensuelle_totale", response.getPrevision_Mensuelle_Totale());
      model.addAttribute("ecart_mensuel_total", response.getEcart_Mensuel_Total());
      model.addAttribute("pourcentage_realisation_mensuelle_totale",
          response.getPourcentage_Realisation_Mensuelle_Totale());
      model.addAttribute("croissance_totale", response.getCroissanceTotale());
    });

    // model.addAttribute("response", responses);
    return "layout/rapport3::summarizedReports";

  }

  @PostMapping(value = "/reportFourGraph")
  @ResponseBody
  public String displayRecetteSummaryUsingChartJsPieReport(@RequestBody ReportFourForm reportFourForm, Model model) {

    System.out.println(" Annee budget selectionnE " + reportFourForm.getAnneeBudgetaireSelected());
    System.out.println(" Mois selectionnE " + reportFourForm.getMonthAnneeBudgetaireSelected());
    Long idExercice = reportFourForm.getAnneeBudgetaireSelected();
    LocalDate monthStartingDate = LocalDate.parse(reportFourForm.getMonthAnneeBudgetaireSelected().split("\\|")[0],
        DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    LocalDate monthEndingDate = LocalDate.parse(reportFourForm.getMonthAnneeBudgetaireSelected().split("\\|")[1],
        DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    LocalDate monthStartingDatePrevYear = monthStartingDate.minusYears(1);
    LocalDate monthEndingDatePrevYear = null;
    if (monthStartingDate.minusYears(1).getMonthValue() == 2 && monthStartingDate.minusYears(1).isLeapYear()) {
      monthEndingDatePrevYear = monthEndingDate.minusYears(1).plusDays(1);
    } else {
      monthEndingDatePrevYear = monthEndingDate.minusYears(1);
    }
    int previsionMonthId = monthStartingDate.getMonth().getValue() - 1;

    System.out.println(" previsionMonthId " + previsionMonthId);
    List<SituationConsolideeResponse> responses = situationConsolidee.findSituationConsolidee(idExercice,
        monthStartingDate, monthEndingDate, monthStartingDatePrevYear, monthEndingDatePrevYear, previsionMonthId);
    JsonObject data = new JsonObject();
    responses.forEach(response -> {
      Long ctiData = 0L;
      Long cdaData = 0L;
      ctiData = response.getRealisation_Mensuelle_CTI();
      Long ctiPrevision = response.getPrevision_Mensuelle_CTI();
      Long cdaPrevision = response.getPrevision_Mensuelle_CDA();
      cdaData = response.getRealisation_Mensuelle_CDA();
      data.addProperty("ctiData", ctiData);
      data.addProperty("cdaData", cdaData);
      data.addProperty("ctiPrevision", ctiPrevision);
      data.addProperty("cdaPrevision", cdaPrevision);
    });
    String resultData = data.toString();
    return resultData;
  }

  @GetMapping("/export/reportOne/excel")
  public void exportExcelReportOne(HttpServletResponse httpServletResponse,
      @ModelAttribute("reportOneFormRequested") ReportOneForm reportOneFormRequestedBefore) throws IOException {
    ReportOneExcelExporter excelExporter = new ReportOneExcelExporter();
    httpServletResponse.setContentType("application/octet-stream");
    httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + "\"rapportJournalier"
        + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xlsx\"");

    List<ReportOneProjection> reportOneDtos = reportService
        .findDailyRecordsUpToJMinusOneForExport(reportOneFormRequestedBefore);
    excelExporter.setReportOneProjections(reportOneDtos);
    excelExporter.exportReportOne(httpServletResponse, reportOneFormRequestedBefore);
  }

  @GetMapping("/export/reportTwo/excel")
  public void exportExcelReportTwo(HttpServletResponse httpServletResponse,
      @ModelAttribute("reportTwoFormRequested") ReportTwoDTO reportTwoDTO) throws IOException {
    ReportOneExcelExporter excelExporter = new ReportOneExcelExporter();
    httpServletResponse.setContentType("application/octet-stream");
    httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + "\"rapportJournalier"
        + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".xlsx\"");

    List<ReportOneProjection> reportOneDtos = reportService.findRecordsWithinPeriodForExport(reportTwoDTO);
    excelExporter.setReportOneProjections(reportOneDtos);
    excelExporter.exportReportTwo(httpServletResponse, reportTwoDTO);
  }

  @ModelAttribute("reportOneFormRequested")
  public ReportOneForm getReportOneFormRequested() {
    return new ReportOneForm();
  }

  @GetMapping("/rebuildMonth/{anneeBudgetaireId}")
  public String rebuildMonthWithInAnnEeBudgetaire(@PathVariable Long anneeBudgetaireId, Model model) {
    System.out.println("ANNEE ID " + anneeBudgetaireId);
    Exercice anneeBudgetaire = anneeBudgetaireService.findById(anneeBudgetaireId);
    model.addAttribute("moisAnneeBudgetaires", buildMonthWithAnneeBudgetaire(anneeBudgetaire));
    return "layout/rapport3::monthOfAnneeBudget";
  }

  @GetMapping(value = "/reportOneReset")
  public String resetReportOne(Model model) {

    model.addAttribute("reportOneFormRequested", null);

    model.addAttribute("dailyReportPage", 0);
    model.addAttribute("dailyReportPageSize", 5);
    model.addAttribute("dailyReportTotalElements", 0);
    model.addAttribute("dailyReportPageTotalPages", 0);
    model.addAttribute("dailyReportPageCurrentPage", 0);
    model.addAttribute("dailyReportTotalAmount", Collections.emptyList());

    return "layout/rapport1::dailyReport";
  }

  @GetMapping(value = "/reportTwoReset")
  public String resetReportTwo(Model model) {

    model.addAttribute("reportTwoFormRequested", null);

    model.addAttribute("dailyReportPage", 0);
    model.addAttribute("dailyReportPageSize", 5);
    model.addAttribute("dailyReportTotalElements", 0);
    model.addAttribute("dailyReportPageTotalPages", 0);
    model.addAttribute("dailyReportPageCurrentPage", 0);
    model.addAttribute("dailyReportTotalAmount", Collections.emptyList());

    return "layout/rapport2::incomesReports";
  }
}
