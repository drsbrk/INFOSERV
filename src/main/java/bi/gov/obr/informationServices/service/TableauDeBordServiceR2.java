package bi.gov.obr.informationServices.service;

import java.util.List;

import bi.gov.obr.informationServices.dto.DashboardTwoResponse;
import bi.gov.obr.informationServices.dto.TableauDeBordOneDTO;

public interface TableauDeBordServiceR2 {

  List<DashboardTwoResponse> findDailyRecordsUpToJMinusOneByPage(TableauDeBordOneDTO tableauDeBordOneDTO);
}
