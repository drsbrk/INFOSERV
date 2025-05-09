package bi.gov.obr.informationServices.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import bi.gov.obr.informationServices.dto.RoleDetailsDTO;
import bi.gov.obr.informationServices.entity.Role;
import bi.gov.obr.informationServices.entity.RoleDetails;
import bi.gov.obr.informationServices.repository.RoleRepository;
import bi.gov.obr.informationServices.service.MainMenuService;
import bi.gov.obr.informationServices.service.RoleDetailsService;
import bi.gov.obr.informationServices.service.RoleService;

@Controller
public class RoleController {
  @Autowired
  private RoleService roleService;
  @Autowired
  private RoleDetailsService roleDetailsService;

  @Autowired
  private MainMenuService mainMenuService;
  @Autowired
  private RoleRepository roleRepo;
  @GetMapping(value = "/roles", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)

  public ResponseEntity<List<Role>> findAll() {
    List<Role> roles = roleService.findAll();
    try {
      return new ResponseEntity<>(roles, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @ResponseBody
  @Secured({ "ROLE_ADMIN" })
  @PostMapping(value = "/roles/retrieveRole", consumes = "text/plain", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
  public ResponseEntity<Role> retrieveRole(@RequestBody String roleCode, Model model) {
    Role role = roleRepo.findByName(roleCode);
    System.out.println("ROLE = " + role);
    if (role != null) {

      return ResponseEntity.ok(role);
    } else {
      System.out.println("NO ROLE WAS FOUND FOR ROLE " + roleCode);
      return ResponseEntity.ok(null);
    }
  }

  @ResponseBody
  @GetMapping(value = "/roles/show/{roleId}/{menuId}")
  public ResponseEntity<List<RoleDetails>> saveUserRoleDetails(@PathVariable(name = "roleId") int roleID,
      @PathVariable(name = "menuId") int menuId) {
    try {

      Optional<List<RoleDetails>> roleDetailsFromDB = roleDetailsService.findRoleMenuDetails(roleID, menuId);
      if (roleDetailsFromDB.isPresent()) {
        List<RoleDetails> list = roleDetailsFromDB.get();
        return ResponseEntity.ok(list);
      }else {
        return ResponseEntity.noContent().build();
      }
    } catch (Exception e) {
      System.out.println("Message d'erreur: " + e.getMessage());
      return ResponseEntity.noContent().build();
    }
  }

  @ResponseBody
  @PostMapping(value = "/roles/functions/save", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<String>> saveUserRoleDetails(@RequestBody RoleDetailsDTO roleDetailsDTO) {
    try {

      // Retrieve what's already in the role_details table for the provided roleId and
      // menuId and make update
      roleDetailsService.deleteBycol1roleIdAndcol2menuId(roleDetailsDTO.getRoleId(), roleDetailsDTO.getMenuId());
      List<Integer> listSubMenusIntegers = roleDetailsDTO.getSubMenuList();
      System.out.println("SUB MENUS ARE: " + roleDetailsDTO.getSubMenuList());
      for (int i = 0; i < listSubMenusIntegers.size(); i++) {
        RoleDetails roleDetails = new RoleDetails();
        roleDetails.setMenu_id(roleDetailsDTO.getMenuId());
        roleDetails.setRole_id(roleDetailsDTO.getRoleId());
        System.out.println("i = " + listSubMenusIntegers.get(i));
        roleDetails.setSubMenuId(listSubMenusIntegers.get(i));
        roleDetailsService.saveRoleDetails(roleDetails);
      }
      List<String> list = new ArrayList<>();
      list.add("OK");
      return ResponseEntity.ok(list);
    } catch (Exception e) {
      System.out.println("Message d'erreur: " + e.getMessage());
      return ResponseEntity.noContent().build();
    }
  }
}
