package bi.gov.obr.informationServices.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import bi.gov.obr.informationServices.entity.MainMenu;
import bi.gov.obr.informationServices.entity.Role;
import bi.gov.obr.informationServices.entity.SubMenu;
import bi.gov.obr.informationServices.entity.User;
import bi.gov.obr.informationServices.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepo;
  @Autowired
  BCryptPasswordEncoder encoder;
  @Autowired
  MainMenuService mainMenuService;
  @Autowired
  RoleDetailsService roleDetailsService;
  @Autowired
  SubMenuService subMenuService;

  @Override
  public String ariaControls(String input) {
    return input.substring(0, 5);
  }

  // @Override
  //
  // public String buildMenu(@AuthenticationPrincipal UserDetails userDetails) {
  // String email = userDetails.getUsername();
  // User user = userRepo.findByEmail(email);
  // Role role = user.getRole(); // profil utilisateur
  // List<MainMenu> menusList =
  // role.getMenus().stream().distinct().collect(Collectors.toList());
  // Collections.sort(menusList);
  // StringBuffer sessionMenuBuffer = new StringBuffer(
  // "<div class=\"d-flex\">\r\n" + " <button id=\"toggle-btn\"
  // type=\"button\">\r\n"
  // + " <i class=\"lni lni-grid-alt\"></i>\r\n" + " </button>\r\n"
  // + " <div class=\"sidebar-logo\">\r\n" + " <a href=\"#\">OBR->BI</a>\r\n"
  // + " </div>\r\n" + " </div><ul class=\"sidebar-nav\">");
  // Iterator<MainMenu> mainMenuIterator = menusList.iterator();
  //
  // while (mainMenuIterator.hasNext()) {
  // MainMenu menu = mainMenuIterator.next();
  // System.out.println("LIBELLE MENU= " + menu.getName());
  // menu.getSubMenus().forEach(smn -> {
  // System.out.println(">>> SUB MENU = " + smn.getDisplayName());
  // });
  // int subMenuSize = menu.getSubMenus().size();
  //
  // if (subMenuSize == 0) {// IF Current main menu doesn't have subMenus
  // sessionMenuBuffer.append("<li class=\"sidebar-item\"><a href='#'
  // class=\"sidebar-link\"><i class='"
  // + menu.getIcon() + "'></i><span>" + menu.getName() + "</span></a></li>\r\n");
  //
  // } else { // If Current main menu has sub menus
  // sessionMenuBuffer.append(
  // "<li class=\"sidebar-item\"><a href=\"#\" class=\"sidebar-link has-dropdown
  // collapsed\" data-bs-toggle=\"collapse\" data-bs-target=\"#"
  // + ariaControls(menu.getName()) + "\" aria-expanded=\"false\"
  // aria-controls=\""
  // + ariaControls(menu.getName()) + "\"><i class=\"" + menu.getIcon() +
  // "\"></i>\r\n"
  // + " <span>" + menu.getName() + "</span></a><ul id=\"" +
  // ariaControls(menu.getName())
  // + "\" class=\"sidebar-dropdown list-unstyled collapse\"
  // data-bs-parent=\"#sidebar\">");
  //
  // // Parcourir la liste des sous menus personnalisés, pas tous les ss menus du
  // // menu principal
  // Set<SubMenu> subMenus = menu.getSubMenus(); // role.getSubMenus()
  // Iterator<SubMenu> subMenuIterator = subMenus.iterator();
  // while (subMenuIterator.hasNext()) {
  //
  // SubMenu subMenu = subMenuIterator.next();
  //
  // sessionMenuBuffer.append("<li class=\"sidebar-item\">\r\n" + " <a href=\""
  // + subMenu.getUri() + "\" class=\"sidebar-link-offset\">\r\n" + " "
  // + subMenu.getDisplayName() + " </a>\r\n" + " </li>");
  // }
  // sessionMenuBuffer.append("</ul></li>");// Fin liste des sous menu du menu
  // principal courant et ferm
  // }
  //
  // }
  // sessionMenuBuffer.append("</ul>");// Fermeture ul du side bar et du sidebar
  // lui même
  // return sessionMenuBuffer.toString();
  // }

  @Override

  public String buildMenu(@AuthenticationPrincipal UserDetails userDetails) {
    String email = userDetails.getUsername();
    User user = userRepo.findByEmail(email);
    Role role = user.getRole(); // profil utilisateur
    // Trouver le role_id et l'utiliser pour trouver les menus auxquels
    // l'utilisateur a accès
    Optional<List<Integer>> menuIdListDetail = Optional
        .ofNullable(roleDetailsService.getAssignedRolePerRole(role.getId()).orElse(null));
    try {
      Set<MainMenu> mainMenus = new HashSet<>();
      if (menuIdListDetail.isPresent()) {
        List<Integer> list = menuIdListDetail.get();
        Iterator<Integer> menuDetailsIterator = list.iterator();
        while (menuDetailsIterator.hasNext()) {
          int menuIdDetail = menuDetailsIterator.next();
          MainMenu menu = mainMenuService.findMainMenuById(menuIdDetail);
          if (menu != null) {
            mainMenus.add(menu);
          }
        }
        List<MainMenu> menusList = mainMenus.stream().distinct().collect(Collectors.toList());
        Collections.sort(menusList);
        StringBuffer sessionMenuBuffer = new StringBuffer(
            "<div class=\"d-flex\">\r\n" + "            <button id=\"toggle-btn\" type=\"button\">\r\n"
                + "                <i class=\"lni lni-grid-alt\"></i>\r\n" + "            </button>\r\n"
                + "            <div class=\"sidebar-logo\">\r\n" + "                <a href=\"#\">OBR->BI</a>\r\n"
                + "            </div>\r\n" + "        </div><ul class=\"sidebar-nav\">");

        Iterator<MainMenu> mainMenuIterator = menusList.iterator();

        while (mainMenuIterator.hasNext()) {
          MainMenu menu = mainMenuIterator.next();
          Optional<List<Integer>> subMenus = roleDetailsService.getAssignedSubMenusPerRoleAndMenu(role.getId(),
              menu.getId());
          List<SubMenu> subMenuEntities = new ArrayList<>();
          if (subMenus.isPresent()) {
            subMenus.get().forEach(sub -> {
              SubMenu subMenu = subMenuService.findSubMenuByID(sub);
              subMenuEntities.add(subMenu);
            });
          } else {

          }
          int subMenuSize = subMenuEntities.size();

          if (subMenuSize == 0) {
            // IF Current main menu doesn't have subMenus, don't display it
            // sessionMenuBuffer.append("<li class=\"sidebar-item\"><a href='#'
            // class=\"sidebar-link\"><i class='"
            // + menu.getIcon() + "'></i><span>" + menu.getName() + "</span></a></li>\r\n");

          } else { // If Current main menu has sub menus
            sessionMenuBuffer.append(
                "<li class=\"sidebar-item\"><a href=\"#\" class=\"sidebar-link has-dropdown collapsed\" data-bs-toggle=\"collapse\" data-bs-target=\"#"
                    + ariaControls(menu.getName()) + "\" aria-expanded=\"false\" aria-controls=\""
                    + ariaControls(menu.getName()) + "\"><i class=\"" + menu.getIcon() + "\"></i>\r\n"
                    + "                    <span>" + menu.getName() + "</span></a><ul id=\""
                    + ariaControls(menu.getName())
                    + "\" class=\"sidebar-dropdown list-unstyled collapse\" data-bs-parent=\"#sidebar\">");

            // Parcourir la liste des sous menus personnalisés, pas tous les sous menus du
            // menu principal
            // Set<SubMenu> subMenus = menu.getSubMenus(); // role.getSubMenus()
            Iterator<SubMenu> subMenuIterator = subMenuEntities.iterator();
            while (subMenuIterator.hasNext()) {

              SubMenu subMenu = subMenuIterator.next();

              sessionMenuBuffer.append("<li class=\"sidebar-item\">\r\n" + "                        <a href=\""
                  + subMenu.getUri() + "\" class=\"sidebar-link-offset\">\r\n" + "                            "
                  + subMenu.getDisplayName() + "                        </a>\r\n" + "                    </li>");
            }
            sessionMenuBuffer.append("</ul></li>");// Fin liste des sous menu du menu principal courant et ferm
          }

        }

        sessionMenuBuffer.append("</ul>");// Fermeture ul du side bar et du sidebar lui même
        return sessionMenuBuffer.toString();
      }
    } catch (Exception e) {
      return null;
    }
    return null;

  }

  @Override
  public List<User> findAllUsers() {
    return userRepo.findAll();
  }

  @Override

  public User findUserByUsername(String username) {
    return userRepo.findByEmail(username);
  }

  @Override
  public boolean isOldPasswordValid(User user, String oldPassword) {
    return encoder.matches(oldPassword, user.getPassword());
  }

  @Override

  public boolean isUserChoosingAssignedPwdFromAdmin(String chosenPwd) {
    String hashedPwd = encoder.encode(chosenPwd);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername(); // Find User
    User user = userRepo.findByEmail(email);
    String assignedPwd = user.getEmail();
    if (hashedPwd.equals(assignedPwd)) {
      // User is taking the same pwd as assigned by admin, so return true
      return true;
    }
    return false;
  }

  @Override
  public boolean performLogin() {

    return false;
  }

  @Override
  public void putNavMenuIntoSessionAndModel(UserDetails userDetails, HttpServletRequest request, Model model) {
    String navMenu = buildMenu(userDetails);
    HttpSession session = request.getSession(); // Retrieve or create session if not exists
    session.setAttribute("navMenu", navMenu);
    model.addAttribute("navMenu", navMenu);
  }

  @Override
  public User saveUser(User user) {
    return userRepo.save(user);
  }

  @Override
  public void toggleEnabled(String email) {

  }

}
