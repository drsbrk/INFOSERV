
package bi.gov.obr.informationServices.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import bi.gov.obr.informationServices.dto.UserActivationDto;
import bi.gov.obr.informationServices.dto.UserPwdDto;
import bi.gov.obr.informationServices.dto.UserSelfPwdChgDto;
import bi.gov.obr.informationServices.entity.MainMenu;
import bi.gov.obr.informationServices.entity.Role;
import bi.gov.obr.informationServices.entity.User;
import bi.gov.obr.informationServices.repository.RoleRepository;
import bi.gov.obr.informationServices.repository.UserRepository;
import bi.gov.obr.informationServices.service.MainMenuService;
import bi.gov.obr.informationServices.service.RoleService;
import bi.gov.obr.informationServices.service.SubMenuService;
import bi.gov.obr.informationServices.service.UserService;

@Controller

public class UserController {

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private RoleRepository roleRepo;

  @Autowired
  private RoleService roleService;

  @Autowired
  private UserService userService;
  @Autowired
  private SubMenuService submenuService;
  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @Autowired
  private MainMenuService mainMenuService;

  @GetMapping(value = "/users/showUserChangePwd")

  // @Secured("hasRole('ROLE_ADMIN')")
  public String changePwd(UserPwdDto pwdUser, Model model) {
    model.addAttribute("pwdDTO", new UserPwdDto());
    return "changePwdAdmin";
  }

  @PostMapping(value = "/users/changePwdAdmin")

  @Secured("hasRole('ROLE_ADMIN')")
  public String changeUserPasswordAsAdmin(@ModelAttribute UserPwdDto pwdDto, RedirectAttributes redirectAttributes) {
    User user = userService.findUserByUsername(pwdDto.getEmail());

    if (user != null) { // User found, just change his/her password // Check if newPass matches
      // confNewPass
      if (!(pwdDto.getNewPassword().equals(pwdDto.getConfNewPassword()))) {
        redirectAttributes.addFlashAttribute("error", "Les deux mots de passe ne sont pas égaux ");
        return "redirect:/users/showUserChangePwd";
      } else { // Les deux mots de passe sont égaux, changer le mot de passe utilisateur

        user.setPassword(passwordEncoder.encode(pwdDto.getConfNewPassword())); // Save user with the new password
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("success", "Réinitialisation du mot de passe réussie ");
        return "redirect:/users/showUserChangePwd";
      }

    } else { // Ceci n'arrive presque jamais car le mail est vérifié côté client
      // User not found with the provided email
      redirectAttributes.addFlashAttribute("error", "Utilisateur introuvable ");
      return "redirect:/users/showUserChangePwd";
    }

  }

  @Secured({ "ROLE_ADMIN" })

  @PostMapping("/users/save")

  public String processRegistration(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
      RedirectAttributes redirectAttrs, Model model)

  {

    if (bindingResult.hasErrors()) { // No processing if form has errors // Find all system roles
      List<Role> roles = roleService.findAll();
      model.addAttribute("roles", roles);
      return "signUp";
    } else {
      if (user.getId() == null) { // New user
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
        String email = user.getEmail();
        email = email.toLowerCase();
        user.setEmail(email);
        String firstName = user.getFirstName();
        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        user.setFirstName(firstName);
        String lastName = user.getLastName();
        lastName = lastName.toUpperCase();
        user.setLastName(lastName);
        user.setEnabled(-1); // user must change her password before getting access to system
        userService.saveUser(user);
        User searchedFromDBUser = userService.findUserByUsername(email);
        if (searchedFromDBUser.getEmail().equals(user.getEmail())) {
          redirectAttrs.addFlashAttribute("success", "Utilisateur créé avec succès!");
          return "redirect:/users/new";
        } else {
          redirectAttrs.addFlashAttribute("error", "Quelque chose a mal tourné pendant la création de l'utilisateur!");
          return "redirect:/users/new";
        }
      } else {

        User usr = userRepo.findById(user.getId()).get();

        usr.setFirstName(user.getFirstName());
        usr.setLastName(user.getLastName());
        usr.setEmail(user.getEmail());
        usr.setRole(user.getRole());
        userService.saveUser(usr);
        redirectAttrs.addFlashAttribute("success", "Utilisateur modifié avec succès");
        return "redirect:/users/new";
      }
    }
  }

  @ResponseBody

  @Secured({ "ROLE_ADMIN" })

  @PostMapping(value = "/users/retrieveUser", consumes = "text/plain", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<User> retrieveUser(@RequestBody String email, Model model) {
    User user = userService.findUserByUsername(email);
    if (user != null) {
      Role role = user.getRole();
      model.addAttribute("user", user);
      model.addAttribute("roles", role);
      return ResponseEntity.ok(user);
    } else {

      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping(value = "/users/role/save")
  public String saveNewRole(@Valid @ModelAttribute("role") Role role,@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request,
      BindingResult result, Model model) {
    userService.putNavMenuIntoSessionAndModel(userDetails, request, model);
    if (result.hasErrors()) {
      List<MainMenu> listMenus = mainMenuService.findAll();
      model.addAttribute("listMainMenus", listMenus);
      return "ajoutRole";
    } else {
      if (role.getId() != null) { // Cas de modification
        Role role2 = roleService.findRole(role.getId()); // Role from Db
        role2.setName(role.getName());
        role2.setRoleName(role.getRoleName());
        // role2.
        role2.setMenus(role.getMenus());
        role.getMenus().forEach(System.out::println);
        System.out.println("ROLE BEING UPDATED :" + role2.getMenus());
        roleRepo.save(role2);
        List<MainMenu> listMenus = mainMenuService.findAll();
        model.addAttribute("listMainMenus", listMenus);
        model.addAttribute("success", " Role " + role.getName() + " modifié");

        return "ajoutRole";
      } else { // Cas d'un nouveau rôle
        if (role.getName().isEmpty() || role.getRoleName().isEmpty()) {
          model.addAttribute("error", "Tous les champs sont obligatoires");

          List<MainMenu> listMenus = mainMenuService.findAll();
          model.addAttribute("listMainMenus", listMenus);
          return "ajoutRole";
        } else {
          Role role2 = new Role();

          role2.setName("ROLE_" + (role.getName()).toUpperCase());
          role2.setRoleName(role.getRoleName());
          role2.setMenus(role.getMenus());

          int element = mainMenuService.findAll().get(4).getId(); // This is authentication menu
          Optional<MainMenu> authentication = Optional.ofNullable(mainMenuService.findMainMenuById(element));
          // Add this main menu to role
          if (authentication.isPresent()) {
            role2.getMenus().add(authentication.get());
          }

          roleRepo.save(role2);
          model.addAttribute("success", "Nouveau rôle ajouté correctement!");
          List<MainMenu> listMenus = mainMenuService.findAll();

          model.addAttribute("listMainMenus", listMenus);
          return "ajoutRole";

        }
      }
    }
  }

  @GetMapping("/users/new")
  @PreAuthorize("hasRole( 'ROLE_ADMIN' )")
  public String show_sign_up_form(Model model) { // Find all system roles

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername(); // Find User
    User user1 = userService.findUserByUsername(email);

    List<Role> allRoles = roleRepo.findAll();
    System.out.println("USER=" + user1.getRole());
    model.addAttribute("roles", allRoles);
    model.addAttribute("user", new User());
    return "signUp";
  }

  @GetMapping({ "/index", "/", "", "/login?logout", "/users/roles/add" })
  public String showIndexPage(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request,
      Model model, Authentication authentication) {
    System.out.println("userdetails=" + userDetails);
    String email = userDetails.getUsername();
    User user = userService.findUserByUsername(email);
    int enabled = user.getEnabled();
    // If enabled == -1, send user to change her password
    if (enabled == -1) {
      return "redirect:/users/changePwdSelf";
    }

    // NavMenu
    String navMenu = userService.buildMenu(userDetails);
    HttpSession session = request.getSession(); // Retrieve or create session if not exists
    session.setAttribute("navMenu", navMenu);
    model.addAttribute("navMenu", navMenu);

    return "index";

  }

  @GetMapping("/login")
  public String showLoginPage(Model model) {
    model.addAttribute("user", new User());
    return "login";
  }

  @GetMapping(value = "/users/roles/showIndexPage")
  public String showRoleAddForm(@RequestParam(value = "id", defaultValue = "", required = false) Integer id,
      Model model) {
    if (id == null) { // Retrieve menus, id = null means all menus
      List<MainMenu> listMainMenus = mainMenuService.findAll();
      model.addAttribute("role", new Role());
      model.addAttribute("listMainMenus", listMainMenus);
      return "ajoutRole";
    } else { // Find the role from DB
      Role role = roleService.findRole(id);
      role.getMenus().forEach(System.out::println);
      List<MainMenu> listMainMenus = mainMenuService.findAll();
      model.addAttribute("listMainMenus", listMainMenus);
      model.addAttribute("role", role);
      return "ajoutRole";
    }
  }

  @Secured("hasRole('ROLE_ADMIN')")
  @GetMapping(value = "/users/changePwdAdmin")
  public String showSimpleAdminChangePwdForm(Model model) {
    UserPwdDto pwdDto = new UserPwdDto();
    model.addAttribute("pwdDTO", pwdDto);
    return "changePwdAdmin";
  }

  @GetMapping(value = "/users/toggle")

  @Secured("hasRole('ROLE_ADMIN')")
  public String showToggleUserStatus(Model model) {
    UserActivationDto user = new UserActivationDto();
    model.addAttribute("user", user);
    return "toggleUserStatus";
  }

  @Secured("hasRole('ROLE_ADMIN')")

  @GetMapping("/users/activation")
  public String showUserActivationForm() {
    return "activation_desactivation";
  }

  @GetMapping(value = "/users/changePwdSelf")
  public String showUserSelfPwdChange(Model model) {
    UserSelfPwdChgDto userPwdDto = new UserSelfPwdChgDto();
    model.addAttribute("pwdDTO", userPwdDto);
    return "changePwdByUser";
  }

  @PostMapping(value = "/users/toggleUserActivation")

  @Secured("hasRole('ROLE_ADMIN')")
  public String toggleUserStatus(UserActivationDto user, RedirectAttributes redirectAttributes,
      BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("error", "Précédente opération contient des erreurs!");
      return "redirect:/users/toggle";
    } else {
      User user2 = userRepo.findById(user.getId()).get();
      if (user2 != null) {

        if (user.getEnabled() == 1) {
          user.setEnabled(0);
        } else {
          user.setEnabled(1);
        }
        user2.setEnabled(user.getEnabled());
        userService.saveUser(user2);
        if (user.getEnabled() == 0) { // Request for deactivation
          redirectAttributes.addFlashAttribute("success", "Utilisateur désactivé avec succès");
          return "redirect:/users/toggle";
        } else {
          redirectAttributes.addFlashAttribute("success", "Utilisateur activé avec succès");
          return "redirect:/users/toggle";
        }

      }
    }
    return null;
  }

  @PostMapping(value = "/users/changePwdSelfDo")
  public String userSelfPasswordChange(RedirectAttributes attr, @ModelAttribute UserSelfPwdChgDto pwdDTO,
      BindingResult result) {
    if (result.hasErrors()) {
      attr.addFlashAttribute("error", "Certains champs ont des valeurs non valides");
      return "changePwdByUser";

    } else { // Find user email from session
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String email = userDetails.getUsername(); // Find User
      User user = userService.findUserByUsername(email); // Check if provided oldPassword is correct

      if (userService.isOldPasswordValid(user, pwdDTO.getOldPassword())) {
        // The two password match, user gets the right to change her pass // Check if
        // newPassword = confNewPassword and not null
        if (pwdDTO.getNewPassword().equals("") || pwdDTO.getConfNewPassword().equals("")) { // Send error to
          // client
          attr.addFlashAttribute("error",
              "Les champs nouveau mot de passe et confirmation nouveau mot de passe ne peuvent être vides");
          return "redirect:/users/changePwdSelf";

        } else {
          if (pwdDTO.getNewPassword().equals(pwdDTO.getConfNewPassword())) {
            if (!passwordEncoder.matches(pwdDTO.getNewPassword(), user.getPassword())) { // Different password
              user.setPassword(passwordEncoder.encode(pwdDTO.getConfNewPassword())); // Save user
              // and inform him/her
              // Check if pwd == -1 and change it to 1
              if (user.getEnabled() == -1) {
                user.setEnabled(1);
                // Save user with that new param
                userService.saveUser(user);
                attr.addFlashAttribute("success", "Mot de passe changé avec succès");
                return "redirect:/users/changePwdSelf";

              }
            } else {
              attr.addFlashAttribute("error",
                  "Veuillez choisir un autre mot de passe car celui que vous êtes en train de choisir vous a déjà été attribué par l'Admin auparavant!");
              return "redirect:/users/changePwdSelf";
            }
          } else {
            attr.addFlashAttribute("error",
                "Le champ nouveau mot de passe n'est pas égal au champ confirmation mot de passe");
            return "redirect:/users/changePwdSelf";
          }
        }
      } else {
        attr.addFlashAttribute("error", "Le mot de passe ancien n'est pas correct!");
        return "redirect:/users/changePwdSelf";

      }
    }
    return null;
  }
}
