package bi.gov.obr.informationServices.controller;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import bi.gov.obr.informationServices.dto.CodeRecetteForm;
import bi.gov.obr.informationServices.dto.GenericResponse;
import bi.gov.obr.informationServices.entity.AppEventLogger;
import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.enums.UserAction;
import bi.gov.obr.informationServices.service.CompteImpotService;
import bi.gov.obr.informationServices.utils.BuildEventLogger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@CrossOrigin()
public class CompteImpotController {

    private final CompteImpotService compteImpotService;

    public CompteImpotController(CompteImpotService compteImpotService) {
        this.compteImpotService = compteImpotService;
    }

    @RequestMapping({"/compteimpot", "compteimpot"})
    public String compteImpot(Model model) {
        model.addAttribute("compteImpot", new CompteImpot());
        model.addAttribute("codeRecetteForm", new CodeRecetteForm());
        return "compteimpot";
    }

    @GetMapping({"/compteimpot/all", "compteimpot/all"})
    public String loadAllCompteImpot(Model model) {
        List<CompteImpot> compteImpots = compteImpotService.findAllCompteImpot();
        model.addAttribute("compteImpots", compteImpots);
        return "compteimpot::all_compteimpot";
    }

    @PostMapping("/compteimpot")
    public ResponseEntity<CompteImpot> postCompteImpot(@Valid CompteImpot compteImpot, BindingResult bindingResult, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        AppEventLogger appEventLogger = BuildEventLogger.buildEventLogger(userDetails.getUsername(), request.getRemoteAddr(), UserAction.ADD_COMPTE_IMPOT, LocalDateTime.now());
        compteImpot = compteImpotService.saveCompteImpot(compteImpot, appEventLogger);
        if (compteImpot == null) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(compteImpot, HttpStatus.OK);
    }


    @PostMapping("/compteimpot/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        String message = "";
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

            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            System.out.println(" =====>> catch catch =====> ");
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("/loadcompteimpot/{id}")
    public ResponseEntity<CompteImpot> loadCompteImpotById(@PathVariable Long id) {
        CompteImpot compteImpot = compteImpotService.findById(id);
        if (compteImpot == null) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(compteImpot, HttpStatus.OK);
    }

    @PutMapping("/compteimpot/update/{id}")
    public ResponseEntity<CompteImpot> updateCompteImpotById(@PathVariable Long id, @RequestBody CompteImpot compteImpot, HttpServletRequest request) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        AppEventLogger appEventLogger = BuildEventLogger.buildEventLogger(userDetails.getUsername(), request.getRemoteAddr(), UserAction.UPDATE_COMPTE_IMPOT, LocalDateTime.now());

        compteImpot = compteImpotService.updateCompteImpot(id, compteImpot, appEventLogger);
        if (compteImpot == null) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(compteImpot, HttpStatus.OK);
    }

    @DeleteMapping("/compteimpot/delete/{id}")
    public ResponseEntity<GenericResponse> deleteCompteImpotById(@PathVariable Long id, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        AppEventLogger appEventLogger = BuildEventLogger.buildEventLogger(userDetails.getUsername(), request.getRemoteAddr(), UserAction.DELETE_COMPTE_IMPOT, LocalDateTime.now());

        boolean isDeleted = compteImpotService.deleteCompteImpot(id);
        if (!isDeleted) {
            return new ResponseEntity<>(GenericResponse.builder().message("Erreur lors de la suppression").statusCode(HttpStatus.EXPECTATION_FAILED.value()).build(), HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(GenericResponse.builder().message("Compte impot supprimé avec succès").statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    }

    //Find By Page
    @GetMapping("/compteimpot/load")
    public String findCompteImpotByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
        Pageable pageable = PageRequest.of(page != 0 ? page - 1 : page, size, Sort.by("id").descending());
        Page<CompteImpot> compteImpots = compteImpotService.findAllCompteImpot(pageable);
        model.addAttribute("compteImpots", compteImpots);
        return "compteimpot::all_compte_impot";
    }

    @GetMapping("/codeRecettes/loadAll")
    public ResponseEntity<List<CompteImpot>> findAllCodeRecettes() {
        List<CompteImpot> codeRecettes = compteImpotService.findAllCompteImpot();
        return new ResponseEntity<>(codeRecettes, HttpStatus.OK);
    }
}