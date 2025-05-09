package bi.gov.obr.informationServices.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomePageController {

  @PostMapping("/failed_login")
  public String HandlefailedLogin() {
    System.out.println("User failed to login");
    return "/login?error";
  }

  @RequestMapping({ "/homepage", "homepage", "homePage", "homePage.html" })
  public String homePage() {
    return "homePage";
  }
}
