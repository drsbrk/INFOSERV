package bi.gov.obr.informationServices.repository;

import org.springframework.data.repository.CrudRepository;

import bi.gov.obr.informationServices.entity.AppSetting;

public interface AppSettingRepository extends CrudRepository<AppSetting, Integer> {
}
