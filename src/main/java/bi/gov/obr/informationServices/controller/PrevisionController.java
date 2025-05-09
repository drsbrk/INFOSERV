package bi.gov.obr.informationServices.controller;

import java.io.IOException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import bi.gov.obr.informationServices.dto.GenericResponse;
import bi.gov.obr.informationServices.dto.PrevisionForm;
import bi.gov.obr.informationServices.dto.PrevisionRecap;
import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.entity.Prevision;
import bi.gov.obr.informationServices.enums.PrevisionType;
import bi.gov.obr.informationServices.service.CompteImpotService;
import bi.gov.obr.informationServices.service.ExerciceService;
import bi.gov.obr.informationServices.service.PrevisionService;
import lombok.RequiredArgsConstructor;

@Controller
@CrossOrigin()
@RequiredArgsConstructor
public class PrevisionController {

  private final PrevisionService previsionService;
  private final ExerciceService exerciceService;
  private final CompteImpotService compteImpotService;
  private int callCount = 0;

  private List<PrevisionRecap> buildPrevisionRecap(List<Prevision> previsions) {
    Map<CompteImpot, List<Prevision>> sortedPrevision = previsions.stream()
        .collect(Collectors.groupingBy(prevision -> prevision.getCodeRecette()));
    List<PrevisionRecap> previsionRecaps = new ArrayList<>();

    sortedPrevision.entrySet().forEach(prevSorted -> {
      PrevisionRecap previsionRecap = new PrevisionRecap();
      previsionRecap.setAnnEeBudgetaire(prevSorted.getValue().get(0).getAnneeBudgetaire());
      previsionRecap.setCodeRecette(prevSorted.getKey().getCode());
      previsionRecap.setId(previsionRecap.getCodeRecette());
      previsionRecap.setLibelleRecette(prevSorted.getKey().getLibelle());
      previsionRecap.setPrevisionDetails(prevSorted.getValue());
      previsionRecaps.add(previsionRecap);
    });

    previsionRecaps.forEach(System.out::println);

    return previsionRecaps;
  }

  @DeleteMapping("/prevision/delete/{id}")
  public ResponseEntity<GenericResponse> deletePrevisionById(@PathVariable Long id) {
    previsionService.deleteById(id);
    return new ResponseEntity<>(GenericResponse.builder().message("Prevision deleted successfully").statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
  }

  //Find By Page
  @GetMapping("/prevision/load")
  public ResponseEntity<Page<Prevision>> findPrevisionByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
    Pageable pageable = PageRequest.of(page != 0 ? page - 1 : page, size, Sort.by("id").descending());
    return new ResponseEntity<>(previsionService.findAll(pageable), HttpStatus.OK);
  }

  @GetMapping("/prevision/loadByAnneeBudgetaire/{anneeBudgetaireId}")
  public ResponseEntity<List<PrevisionRecap>> findPrevisionByPage(@PathVariable Long anneeBudgetaireId, Model model) {
    List<Prevision> previsions = new ArrayList<>();
    previsions = previsionService.loadPrevisionByAnneeBudgetaire(anneeBudgetaireId);
    //        model.addAttribute("previsionDetails", previsions);
    return new ResponseEntity<>(buildPrevisionRecap(previsions),HttpStatus.OK);
  }

  private Prevision getPrevision(PrevisionForm prevision) {
    Prevision previsionSubmitted = new Prevision();
    if (prevision.getId() != null) {
      previsionSubmitted.setId(prevision.getId());
    }
    previsionSubmitted.setCodeRecette(compteImpotService.findById(prevision.getSelectedCodeRecetteId()));
    previsionSubmitted.setAnneeBudgetaire(exerciceService.findById(prevision.getSelectedAnneeBudgetId()));
    Month month = Arrays.stream(Month.values())
        .filter(mois -> mois.getDisplayName(TextStyle.FULL, Locale.FRANCE).equalsIgnoreCase(prevision.getSelectedMonth()))
        .findFirst().orElse(null);
    previsionSubmitted.setPrevisionMonth(month);
    if(prevision.getMontantCda() != null) {
      previsionSubmitted.setMontantCda(prevision.getMontantCda());
    } else {
      previsionSubmitted.setMontantCda(prevision.getMontantCti());
    }

    return previsionSubmitted;
  }

  @GetMapping({"/prevision/all", "prevision/all"})
  public String loadAllPrevision(Model model) {
    List<Prevision> previsions = previsionService.findAll();
    buildPrevisionRecap(previsions);
    model.addAttribute("previsions", previsions);
    return "prevision::all_prevision";
  }

  @GetMapping("/loadAllPrevisionByCodeRecetteAndAnneeBudgetaire/{codeRecetteId}/{anneeBudgetaireId}")
  public String loadAllPrevisionByCodeRecetteAndAnneeBudgetaire(@PathVariable String codeRecetteId, @PathVariable Long anneeBudgetaireId, Model model) {

    System.out.println(" codeRecetteId codeRecetteId " + codeRecetteId);
    System.out.println(" codeRecetteId codeRecetteId " + codeRecetteId);
    System.out.println(" anneeBudgetaireId anneeBudgetaireId " + anneeBudgetaireId);
    System.out.println(" anneeBudgetaireId anneeBudgetaireId " + anneeBudgetaireId);

    List<Prevision> previsions = previsionService.loadPrevisionByAnneeBudgetaire(anneeBudgetaireId, Collections.singletonList(codeRecetteId));
    model.addAttribute("previsionDetailsSize",previsions.size());
    model.addAttribute("previsionDetails",previsions);
    return "layout/previsionDetail::previsionDetail";
  }

  @GetMapping("/authorizePrevisionDeletion/{id}")
  public ResponseEntity<String> loadCodePrevisionById(@PathVariable Long id) {
    String previsionCode = previsionService.findCodePrevisionById(id);
    if (previsionCode == null) {
      return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    return new ResponseEntity<>(previsionCode, HttpStatus.OK);
  }

  @GetMapping("/loadprevision/{id}")
  public ResponseEntity<Prevision> loadPrevisionById(@PathVariable Long id) {
    Prevision prevision = previsionService.findById(id);
    if (prevision == null) {
      return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    return new ResponseEntity<>(prevision, HttpStatus.OK);
  }

  @RequestMapping({"/prevision", "prevision"})
  public String prevision(Model model) {
    model.addAttribute("prevision", new Prevision());
    List<String> typePrevisions = new ArrayList<>();
    Arrays.stream(PrevisionType.values()).forEach(p -> typePrevisions.add(p.name()));
    PrevisionForm previsionForm = new PrevisionForm();
    previsionForm.setCodeRecettes(compteImpotService.findAllCompteImpot());
    previsionForm.setMonths(Arrays.stream(Month.values()).map(month -> StringUtils.capitalize(month.getDisplayName(TextStyle.FULL, Locale.FRANCE))).collect(Collectors.toList()));
    previsionForm.setAnneeBudgetaires(exerciceService.findAllExercice());
    model.addAttribute("previsionForm", previsionForm);
    model.addAttribute("currentAnneeBudgetaire", exerciceService.findCurrentAnneeBudgetaire().getId());
    model.addAttribute("previsionDetailsSize", 0);
    //        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
    //        Paged<Prevision> previsions = previsionService.findAll(pageable);
    //        model.addAttribute("previsions", previsions);
    return "prevision";
  }

  @PostMapping("/prevision")
  public ResponseEntity<Prevision> savePrevision(@RequestBody PrevisionForm prevision) throws IOException {
    Prevision previsionSubmitted = getPrevision(prevision);
    return new ResponseEntity<>(previsionService.save(previsionSubmitted), HttpStatus.OK);
  }

  @PutMapping("/prevision/update/{id}")
  public ResponseEntity<Prevision> updatePrevisionById(@PathVariable Long id, @RequestBody PrevisionForm prevision) {

    Prevision previsionToUpdate = getPrevision(prevision);
    previsionToUpdate = previsionService.update(id, previsionToUpdate);
    if (previsionToUpdate == null) {
      return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    return new ResponseEntity<>(previsionToUpdate, HttpStatus.OK);
  }

  @PostMapping("/prevision/upload/{idPrevision}")
  public ResponseEntity<String> uploadPrevisionFile(@PathVariable Long idPrevision, @RequestParam("file") MultipartFile file) {

    String message;

    if (file.isEmpty()) {
      message = "Please select a file to upload.";
      return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    Prevision prevision = previsionService.findById(idPrevision);


    try {
      // Create a Workbook from an Excel file (.xls or .xlsx)
      Workbook workbook = new XSSFWorkbook(file.getInputStream());
      // Get the first Sheet from workbook
      Sheet sheet = workbook.getSheetAt(0);
      // Iterate through each rows one by one
      for (Row row : sheet) {
        // For each row, iterate through all the columns
        for (Cell cell : row) {
          // Check the cell type and format accordingly
          switch (cell.getCellType()) {
          case NUMERIC:
            System.out.print(cell.getNumericCellValue() + "\t");
            break;
          case STRING:
            System.out.print(cell.getStringCellValue() + "\t");
            break;
          }
        }
      }
      workbook.close();

      message = "Prévision enregistrée avec succès";
      return new ResponseEntity<>(message, HttpStatus.OK);

    } catch (Exception e) {
      System.out.println(" =====>> catch catch =====> ");
      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
    }


  }
}