package bi.gov.obr.informationServices.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;

import bi.gov.obr.informationServices.config.AppSecurityConfig;
import bi.gov.obr.informationServices.entity.MainMenu;
import bi.gov.obr.informationServices.entity.RoleDetailDTO;
import bi.gov.obr.informationServices.entity.RoleDetails;
import bi.gov.obr.informationServices.entity.RoleDetailsPK;
import bi.gov.obr.informationServices.entity.SubMenu;
import bi.gov.obr.informationServices.repository.MainMenuRepository;
import bi.gov.obr.informationServices.service.MainMenuService;
import bi.gov.obr.informationServices.service.RoleDetailsService;
import bi.gov.obr.informationServices.service.RoleService;
import bi.gov.obr.informationServices.service.SubMenuService;
import bi.gov.obr.informationServices.service.UserService;

@Controller
@RequestMapping(value = "mainmenu")

public class MainMenuController {
  @Autowired
  private MainMenuService mainMenuService;
  @Autowired
  private SubMenuService subMenuService;

  @Autowired
  private UserService userService;
  @Autowired
  private MainMenuRepository mainRepo;
  @Autowired
  private RoleService roleService;
  @Autowired
  private RoleDetailsService roleDetailsService;
  @PostMapping(value = "/add")
  public String addMainMenu(@Valid @ModelAttribute("menu") MainMenu mainMenu, BindingResult bindingResult,
      Model model, @AuthenticationPrincipal UserDetails userDetails,
      @RequestParam(value = "id", required = false) Integer id) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("error", "Erreurs dans le formulaire");
      model.addAttribute("soumenus", subMenuService.findAll());
      model.addAttribute("allMenus", mainMenuService.findAll());
      return "mainMenuForm";
    } else {
      // If id is null, i.e add, else update
      if (mainMenu.getId() == null) {
        MainMenu menu = new MainMenu();
        menu.setCreatedBy(userService.findUserByUsername(userDetails.getUsername()).getFirstName()+" "+userService.findUserByUsername(userDetails.getUsername()).getLastName());
        menu.setCreationDate(AppSecurityConfig.getCurrentDate());
        menu.setIcon(mainMenu.getIcon());
        menu.setName(mainMenu.getName());
        menu.setSubMenus(mainMenu.getSubMenus());
        System.out.println("SUB MENUS UNDER ATTACHEMENT ARE: " + mainMenu.getSubMenus());
        menu.setUri(mainMenu.getUri());
        MainMenu m = mainMenuService.addNewMainMenu(menu);
        System.out.println("SAVED MAIN MENU=" + m);
        if (m != null) {
          model.addAttribute("soumenus", subMenuService.findAll());
          model.addAttribute("allMenus", mainMenuService.findAll());
          model.addAttribute("success", "Succès d'ajout nouveau menu principal");
          return "mainMenuForm";
        } else {
          model.addAttribute("soumenus", subMenuService.findAll());
          model.addAttribute("allMenus", mainMenuService.findAll());
          model.addAttribute("error", "Echec d'enregistrement du nouveau menu principal");
          return "mainMenuForm";
        }

      } else {
        MainMenu menu = mainMenuService.findMainMenuById(id);
        System.out.println("Main menu to update=" + menu);
        menu.setCreatedBy(userService.findUserByUsername(userDetails.getUsername()).getFirstName() + " "
            + userService.findUserByUsername(userDetails.getUsername()).getLastName());
        menu.setCreationDate(AppSecurityConfig.getCurrentDate());
        menu.setIcon(mainMenu.getIcon());
        menu.setName(mainMenu.getName());
        menu.setId(mainMenu.getId());
        menu.setSubMenus(mainMenu.getSubMenus());
        System.out.println("SUB MENUS UNDER ATTACHEMENT ARE: " + mainMenu.getSubMenus());
        menu.setUri(mainMenu.getUri());

        MainMenu m1 = mainRepo.save(menu);

        if (m1 != null) {
          model.addAttribute("soumenus", subMenuService.findAll());
          model.addAttribute("allMenus", mainMenuService.findAll());
          model.addAttribute("success", "Succès de modification du menu principal");
          return "mainMenuForm";
        }


        model.addAttribute("soumenus", subMenuService.findAll());
        model.addAttribute("allMenus", mainMenuService.findAll());
        model.addAttribute("error", "Echec de modification du menu principal");
        return "mainMenuForm";
      }
    }
  }

  @GetMapping(value = "/fonctions")
  public String afficherPageGestionFonctions(Model model) {
    model.addAttribute("roleDetailsPK", new RoleDetailsPK());
    model.addAttribute("allRoles", roleService.findAll());
    model.addAttribute("menus", mainMenuService.findAll());
    model.addAttribute("fonctions", subMenuService.findAll());
    return "fonctionnalites";
  }

  @ResponseBody
  @Secured({ "ROLE_ADMIN" })
  @GetMapping(value = "/menu", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<MainMenu>> findAll() {
    try {
      return new ResponseEntity<>(mainMenuService.findAll(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @ResponseBody
  @PostMapping(value = "/search/menu", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  public ResponseEntity<Optional<MainMenu>> getMainMenuDetails(@RequestBody MainMenu menuMainMenu)
      throws JsonProcessingException {

    try {
      System.out.println("SENT DATA:" + menuMainMenu.getId());
      Optional<MainMenu> menu = Optional.ofNullable(mainMenuService.findMainMenuById(menuMainMenu.getId()));

      return ResponseEntity.ok(menu);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.ok(null);
    }
  }

  //  @ResponseBody
  //  @PostMapping(value = "/roleDetails", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  //  public ResponseEntity<Set<SubMenu>> getMainMenuSubMenus(@RequestBody MainMenu menuMainMenu)
  //      throws JsonProcessingException {
  //
  //    try {
  //      if (menuMainMenu.getId().equals(0)) {
  //        System.out.println("Menu Id = " + menuMainMenu.getId() + " does not exist in menus table");
  //      }
  //      System.out.println("SENT DATA:" + menuMainMenu.getId());
  //      Optional<MainMenu> menu = mainMenuService.findMainMenuById(menuMainMenu.getId());
  //
  //      // Find associated sub menus for given main menu
  //      if (menu != null) {
  //        Set<SubMenu> subMenuSet = menu.get().getSubMenus();
  //        return ResponseEntity.ok(subMenuSet);
  //      } else {
  //        System.out.println("Empty object = " + ResponseEntity.ok(null));
  //        return ResponseEntity.ok(null);
  //      }
  //
  //
  //    } catch (Exception e) {
  //      e.printStackTrace();
  //      return ResponseEntity.ok(null);
  //    }
  //  }

  @ResponseBody
  @PostMapping(value = "/roleDetails", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  public ResponseEntity<Set<SubMenu>> getMainMenuSubMenus(@RequestBody MainMenu menuMainMenu)
      throws JsonProcessingException {

    try {
      if (menuMainMenu.getId().equals(0)) {
        System.out.println("Menu Id = " + menuMainMenu.getId() + " does not exist in menus table");
      }
      System.out.println("SENT DATA:" + menuMainMenu.getId());
      Optional<MainMenu> menu = Optional.ofNullable(mainMenuService.findMainMenuById(menuMainMenu.getId()));

      // Find associated sub menus for given main menu
      if (menu.isPresent()) {
        Set<SubMenu> subMenuSet = menu.get().getSubMenus();
        System.out.println("List menus = " + subMenuSet);
        return ResponseEntity.ok(subMenuSet);
      } else {
        System.out.println("Empty object = " + ResponseEntity.ok(null));
        return ResponseEntity.ok(null);
      }


    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.ok(null);
    }
  }

  @PostMapping(value = "/role/details/seach", consumes = MimeTypeUtils.APPLICATION_JSON_VALUE, produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Optional<List<RoleDetails>>> searchRoleDetails(@RequestBody RoleDetailDTO roleDetailDTO)
      throws JsonProcessingException {
    try {
      System.out.println("role_id = " + roleDetailDTO.getRole_id());
      System.out.println("menu_id = " + roleDetailDTO.getMenu_id());
      System.out.println("Received obj = " + roleDetailDTO);
      Optional<List<RoleDetails>> roleDetails = roleDetailsService.findRoleMenuDetails(roleDetailDTO.getRole_id(),
          roleDetailDTO.getMenu_id());
      System.out.println("Role Details = " + roleDetails);
      System.out.println("RoleDetails lines: " + roleDetails);
      return ResponseEntity.ok(roleDetails);
    } catch (Exception e) {

      e.printStackTrace();
      return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

  }

  @GetMapping(value = "/show" )
  public String showAddMainMenu(@RequestParam(value = "id", required = false) String id, Model model) {
    if(id==null) {
      // Show context
      model.addAttribute("menu", new MainMenu());
      List<SubMenu> soumenus = subMenuService.findAll();
      model.addAttribute("soumenus", soumenus);
      model.addAttribute("allMenus", mainMenuService.findAll());
      return "mainMenuForm";
    } else {
      System.out.println("GIVEN STRING=" + id);
      // Update context
      Optional<MainMenu> menu = mainMenuService.findMainMenuById(id);
      if (menu.isPresent()) {
        model.addAttribute("menu", menu);
      }

      List<SubMenu> soumenus = subMenuService.findAll();
      model.addAttribute("allMenus", mainMenuService.findAll());
      model.addAttribute("soumenus", soumenus);
      return "mainMenuForm";
    }
  }
}

