package bi.gov.obr.informationServices.controller;

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

import bi.gov.obr.informationServices.dto.GenericResponse;
import bi.gov.obr.informationServices.entity.AppEventLogger;
import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.enums.UserAction;
import bi.gov.obr.informationServices.service.ExerciceService;
import bi.gov.obr.informationServices.utils.BuildEventLogger;
import bi.gov.obr.informationServices.utils.Paged;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@CrossOrigin()
public class ExerciceController {

    private final ExerciceService exerciceService;

    public ExerciceController(ExerciceService exerciceService) {
        this.exerciceService = exerciceService;
    }

    @RequestMapping({"/exercice", "exercice"})
    public String exercice(Model model) {
        model.addAttribute("exercice", new Exercice());
        return "exercice";
    }

    @GetMapping({"/exercice/all", "exercice/all"})
    public ResponseEntity<List<Exercice>> loadAllexercice(Model model) {
        List<Exercice> exercices = exerciceService.findAllExercice();
        return new ResponseEntity<>(exercices, HttpStatus.OK);
    }

    @PostMapping("/exercice")
    public ResponseEntity<Exercice> postExercice(@Valid Exercice exercice, BindingResult bindingResult, HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        AppEventLogger appEventLogger = BuildEventLogger.buildEventLogger(userDetails.getUsername(), request.getRemoteAddr(), UserAction.ADD_EXERCICE, LocalDateTime.now());
        exercice = exerciceService.saveExercice(exercice, appEventLogger);
        if (exercice == null) {
            return new ResponseEntity<>(exercice, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(exercice, HttpStatus.OK);
    }

    @GetMapping("/loadexercice/{id}")
    public ResponseEntity<Exercice> loadExerciceById(@PathVariable Long id) {

        Exercice exercice = exerciceService.findById(id);
        if (exercice == null) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(exercice, HttpStatus.OK);
    }

    @PutMapping("/exercice/update/{id}")
    public ResponseEntity<Exercice> updateExerciceById(@PathVariable Long id, @RequestBody Exercice exercice, HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        AppEventLogger appEventLogger = BuildEventLogger.buildEventLogger(userDetails.getUsername(), request.getRemoteAddr(), UserAction.UPDATE_EXERCICE, LocalDateTime.now());

        exercice = exerciceService.updateExercice(id, exercice, appEventLogger);
        if (exercice == null) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(exercice, HttpStatus.OK);
    }

    @DeleteMapping("/exercice/delete/{id}")
    public ResponseEntity<GenericResponse> deleteExerciceById(@PathVariable Long id, HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        AppEventLogger appEventLogger = BuildEventLogger.buildEventLogger(userDetails.getUsername(), request.getRemoteAddr(), UserAction.DELETE_EXERCICE, LocalDateTime.now());

        boolean isDeleted = exerciceService.deleteExercice(id);
        if (!isDeleted) {
            return new ResponseEntity<>(GenericResponse.builder().message("Erreur lors de la suppression").statusCode(HttpStatus.EXPECTATION_FAILED.value()).build(), HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(GenericResponse.builder().message("Exercice supprimé avec succès").statusCode(HttpStatus.OK.value()).build(), HttpStatus.OK);
    }

    //Find By Page
    @GetMapping("/exercice/load")
    public String findExerciceByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
        System.out.println("Page requested : " + page + " with size : " + size);
        Pageable pageable = PageRequest.of(page != 0 ? page - 1 : page, size, Sort.by("id").descending());
        Paged<Exercice> exercices = exerciceService.findAllExercice(pageable);
        System.out.println("When I reload the page, I get this");
        exercices.getPage().forEach(System.out::println);
        model.addAttribute("exercices", exercices);
        return "exercice::all_exercice";
    }

    @GetMapping("/exercice/current")
    public ResponseEntity<Exercice> findCurrentExercice(){
        Exercice exercice = exerciceService.findExerciceByDateBetween(LocalDate.now());
        return new ResponseEntity<>(exercice, HttpStatus.OK);
    }
}