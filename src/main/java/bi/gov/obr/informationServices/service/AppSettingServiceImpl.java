package bi.gov.obr.informationServices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.entity.AppSetting;
import bi.gov.obr.informationServices.repository.AppSettingRepository;

@Service
@RequiredArgsConstructor
public class AppSettingServiceImpl implements AppSettingService {

    private final AppSettingRepository appSettingRepository;

    @Override
    public AppSetting getAppSetting() {
        try {
           return appSettingRepository.findAll().iterator().next();
        } catch (Exception e) {
           return new AppSetting();
        }

    }

    @Override
    public AppSetting setAppSetting(AppSetting appSetting) {
        try {
            appSetting = appSettingRepository.save(appSetting);
        } catch (Exception e) {
            throw new IllegalArgumentException("Les paramètres de l'application n'ont pas été mis à jour correctement.");
        }
        return appSetting;
    }
}
