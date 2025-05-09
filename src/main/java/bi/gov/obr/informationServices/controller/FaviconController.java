package bi.gov.obr.informationServices.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
final class FaviconController {

  @GetMapping("favicon.ico")
  @ResponseBody
  void returnNoFavicon() {
  }
}
