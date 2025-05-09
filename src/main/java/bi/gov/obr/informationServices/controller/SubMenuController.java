package bi.gov.obr.informationServices.controller;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import bi.gov.obr.informationServices.config.AppSecurityConfig;
import bi.gov.obr.informationServices.entity.MainMenu;
import bi.gov.obr.informationServices.entity.SubMenu;
import bi.gov.obr.informationServices.service.MainMenuService;
import bi.gov.obr.informationServices.service.SubMenuService;
import bi.gov.obr.informationServices.service.UserService;

@Controller
public class SubMenuController {
  @Autowired
  private MainMenuService mainMenuService;

  @Autowired
  private SubMenuService subMenuService;

  @Autowired
  private UserService userService;


  @PostMapping("/sousmenu/add")
  public String addNewSubMenu(@ModelAttribute("submenu") @Valid SubMenu subMenu,
      @AuthenticationPrincipal UserDetails userDetails, BindingResult result, Model model) {
    userService.buildMenu(userDetails);
    if (result.hasErrors()) {
      model.addAttribute("error", "Erreur d'ajout du nouveau sous menu " + subMenu.getDisplayName());
      return "submenuForm";
    } else {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      subMenu.setCreationDate(AppSecurityConfig.getCurrentDate());
      subMenu.setCreatedBy(userService.findUserByUsername(authentication.getName()).getFirstName() + " "
          + userService.findUserByUsername(authentication.getName()).getLastName());

      subMenuService.save(subMenu);
      model.addAttribute("success", "Nouveau sous menu \"" + subMenu.getDisplayName() + "\" ajouté!");

      return "submenuForm";
    }
  }

  @ResponseBody

  @Secured({ "ROLE_ADMIN" })
  @Transactional
  @GetMapping(value = "/sousmenus/search", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  public ResponseEntity<Iterable<SubMenu>> findAll() {
    try { // echo //
      List<SubMenu> subMenus = subMenuService.findAll();

      /*
       * for (SubMenu subMenu : subMenus) { System.out.println("SUBMENU=" + subMenu);
       * }
       */
      System.out.println("JSON DATA: " + subMenus);
      return new ResponseEntity<>(subMenus, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  //  @Secured({ "ROLE_ADMIN" })
  //  @GetMapping(value = "/sousmenus/search")
  //  public @ResponseBody Map<String, List<SubMenu>> findAll() {
  //    try {
  //      Map<String, List<SubMenu>> result = new HashMap<>();
  //      result.put("SubMenu", subMenuService.findAll());
  //      System.out.println("Sous menus for table sousmenus = " + result);
  //      return result;
  //
  //    } catch (Exception e) {
  //      return null;
  //    }
  //  }

  @GetMapping("/sousmenu/show")
  public String showFonctionnaliteForm(@RequestParam(name = "id", defaultValue = "") Integer id, Model model,
      @AuthenticationPrincipal UserDetails userDetails) {
    userService.buildMenu(userDetails);
    if (id == null) {
      List<MainMenu> mainMenus = mainMenuService.findAll();
      model.addAttribute("mainMenus", mainMenus);
      model.addAttribute("submenu", new SubMenu());
      return "submenuForm";
    } else {
      // Only one record is needed for update
      List<MainMenu> mainMenus = mainMenuService.findAll();
      model.addAttribute("mainMenus", mainMenus);
      SubMenu subMenu = subMenuService.findSubMenuByID(id);
      model.addAttribute("submenu", subMenu);
      return "submenuForm";
    }

  }

}
