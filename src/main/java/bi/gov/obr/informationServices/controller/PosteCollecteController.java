package bi.gov.obr.informationServices.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import bi.gov.obr.informationServices.dto.PosteCollecteResponse;
import bi.gov.obr.informationServices.entity.AppEventLogger;
import bi.gov.obr.informationServices.entity.PosteCollecte;
import bi.gov.obr.informationServices.enums.UserAction;
import bi.gov.obr.informationServices.service.PosteCollecteService;
import bi.gov.obr.informationServices.utils.BuildEventLogger;
import bi.gov.obr.informationServices.utils.Paged;

@Controller
@CrossOrigin()
public class PosteCollecteController {

  private final PosteCollecteService posteCollecteService;
  private final UserDetailsService userDetailsService;

  public PosteCollecteController(PosteCollecteService posteCollecteService, UserDetailsService userDetailsService) {
    this.posteCollecteService = posteCollecteService;
    this.userDetailsService = userDetailsService;
  }

  @DeleteMapping("/postecollecte/delete/{id}")
  public ResponseEntity<PosteCollecteResponse> deletePosteCollecteById(@PathVariable Long id, HttpServletRequest request) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    AppEventLogger appEventLogger = BuildEventLogger.buildEventLogger(userDetails.getUsername(), request.getRemoteAddr(), UserAction.DELETE_POSTE_COLLECTE, LocalDateTime.now());

    boolean isDeleted = posteCollecteService.deletePosteCollecte(id);
    if (!isDeleted) {
      return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    PosteCollecteResponse posteCollecteResponse = new PosteCollecteResponse();
    posteCollecteResponse.setPosteCollectes(posteCollecteService.findAllPosteCollecte());
    return new ResponseEntity<>(posteCollecteResponse, HttpStatus.OK);
  }

  //Find By Page
  @GetMapping("/postecollecte/load")
  public String findPosteCollecteByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, Model model) {
    System.out.println("Page requested : " + page + " with size : " + size);
    Pageable pageable = PageRequest.of(page != 0 ? page - 1 : page, size, Sort.by("id").descending());
    Paged<PosteCollecte> posteCollectes = posteCollecteService.findAllPosteCollecte(pageable);
    model.addAttribute("posteCollectes", posteCollectes);
    return "postecollecte::all_poste_collecte";
  }

  @GetMapping({"/postecollecte/all", "postecollecte/all"})
  public String loadAllposteCollecte(Model model) {

    List<PosteCollecte> posteCollectes = posteCollecteService.findAllPosteCollecte();
    model.addAttribute("posteCollectes", posteCollectes);
    return "postecollecte::all_poste_collecte";
  }

  @GetMapping("/loadpostecollecte/{code}")
  public ResponseEntity<PosteCollecte> loadPosteCollecteById(@PathVariable String code) {

    PosteCollecte posteCollecte = posteCollecteService.findByCode(code);
    if (posteCollecte == null) {
      return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    return new ResponseEntity<>(posteCollecte, HttpStatus.OK);
  }

  @PostMapping("/postecollecte")
  public ResponseEntity<PosteCollecte> postCollecte(@Valid PosteCollecte posteCollecte, BindingResult bindingResult, HttpServletRequest request) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    AppEventLogger appEventLogger = BuildEventLogger.buildEventLogger(userDetails.getUsername(), request.getRemoteAddr(), UserAction.ADD_POSTE_COLLECTE, LocalDateTime.now());
    posteCollecte = posteCollecteService.savePosteCollecte(posteCollecte, appEventLogger);
    if (posteCollecte == null) {
      return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    return new ResponseEntity<>(posteCollecte, HttpStatus.OK);
  }

  @RequestMapping({"/postecollecte", "postecollecte"})
  public String posteCollecte(Model model) {
    model.addAttribute("posteCollecte", new PosteCollecte());
    Pageable pageable = PageRequest.of(0, 5, Sort.by("code").descending());
    Paged<PosteCollecte> posteCollectes = posteCollecteService.findAllPosteCollecte(pageable);
    model.addAttribute("posteCollectes", posteCollectes);
    return "postecollecte";
  }

  @PutMapping("/postecollecte/update/{code}")
  public ResponseEntity<PosteCollecteResponse> updatePosteCollecteById(@PathVariable String code,
      @RequestBody PosteCollecte posteCollecte, HttpServletRequest request) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    AppEventLogger appEventLogger = BuildEventLogger.buildEventLogger(userDetails.getUsername(), request.getRemoteAddr(), UserAction.UPDATE_POSTE_COLLECTE, LocalDateTime.now());

    posteCollecte = posteCollecteService.updatePosteCollecte(code, posteCollecte, appEventLogger);
    if (posteCollecte == null) {
      return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    PosteCollecteResponse posteCollecteResponse = new PosteCollecteResponse();
    posteCollecteResponse.setPosteCollecte(posteCollecte);
    posteCollecteResponse.setPosteCollectes(posteCollecteService.findAllPosteCollecte());
    return new ResponseEntity<>(posteCollecteResponse, HttpStatus.OK);
  }
}
