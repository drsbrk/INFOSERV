package bi.gov.obr.informationServices.service;

import bi.gov.obr.informationServices.entity.AppSetting;

public interface AppSettingService {

    AppSetting getAppSetting();
    AppSetting setAppSetting(AppSetting appSetting);
}
