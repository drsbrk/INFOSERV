package bi.gov.obr.informationServices.service;

import java.util.List;

import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.dto.DashboardTwoResponse;
import bi.gov.obr.informationServices.dto.TableauDeBordOneDTO;

@Service
public class TableauDeBordServiceR2Impl implements TableauDeBordServiceR2 {


  @Override
  public List<DashboardTwoResponse> findDailyRecordsUpToJMinusOneByPage(TableauDeBordOneDTO tableauDeBordOneDTO) {

    return null;
  }
}
