package bi.gov.obr.informationServices.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import bi.gov.obr.informationServices.entity.AppSetting;
import bi.gov.obr.informationServices.service.AppSettingService;

@RequiredArgsConstructor
@Controller
public class AppSettingController {

    private final AppSettingService appSettingService;

    @RequestMapping("/appSettings")
    public String getAppSetting(Model model) {

        AppSetting appSetting = appSettingService.getAppSetting();
        model.addAttribute("appSetting", appSetting);
        return "appSettings";
    }

    @PostMapping("/appSettings")
    public String updateAppSetting(@ModelAttribute("appSetting") AppSetting appSetting, Model model) {

        appSetting = appSettingService.setAppSetting(appSetting);
        appSetting.setActual(true);
        System.out.println(appSetting);
        return "appSettings";
    }
}
