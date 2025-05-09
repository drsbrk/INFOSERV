package bi.gov.obr.informationServices.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import bi.gov.obr.informationServices.service.UserService;



public class MenuHelper {
  @Autowired
  private UserService userService;

  // String menu;
  private AuthenticationPrincipal auth;

  public MenuHelper(AuthenticationPrincipal userDetails) {
    auth = userDetails;
  }

  @Bean
  public String getMenu(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
    // NavMenu
    String navMenu = userService.buildMenu(userDetails);

    return navMenu;
  }
}
