package bi.gov.obr.informationServices.service;

import java.util.List;

import bi.gov.obr.informationServices.dto.DashboardOneResponse;
import bi.gov.obr.informationServices.dto.DashboardTwoResponse;
import bi.gov.obr.informationServices.dto.RapportConsolideDTO;
import bi.gov.obr.informationServices.dto.SituationConsolideeResponse;
import bi.gov.obr.informationServices.dto.TableauDeBordOneDTO;
import bi.gov.obr.informationServices.dto.TableauDeBordTwoDTO;

public interface TableauDeBordService {

  List<DashboardOneResponse> findDailyRecordsUpToJMinusOneByPage(TableauDeBordOneDTO tableauDeBordOneDTO);

  List<SituationConsolideeResponse> findSituationConsolidee(RapportConsolideDTO rapportConsolideDto);

  List<DashboardTwoResponse> findSituationPeriodique(TableauDeBordTwoDTO tableauDeBordTwoDTO);
}
