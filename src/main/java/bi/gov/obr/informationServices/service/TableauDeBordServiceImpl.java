package bi.gov.obr.informationServices.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.dto.DashboardOneResponse;
import bi.gov.obr.informationServices.dto.DashboardTwoResponse;
import bi.gov.obr.informationServices.dto.RapportConsolideDTO;
import bi.gov.obr.informationServices.dto.SituationConsolideeResponse;
import bi.gov.obr.informationServices.dto.TableauDeBordOneDTO;
import bi.gov.obr.informationServices.dto.TableauDeBordTwoDTO;
import bi.gov.obr.informationServices.repository.PosteCollecteRepository;

@Service
public class TableauDeBordServiceImpl implements TableauDeBordService {
  @Autowired
  private PosteCollecteRepository posteRepo;
  @Override
  public List<DashboardOneResponse> findDailyRecordsUpToJMinusOneByPage(TableauDeBordOneDTO tableauDeBordOneDTO) {
    LocalDate selectedDate = tableauDeBordOneDTO.getSelectedDate();
    LocalDate debutMois = selectedDate.withDayOfMonth(1);
    LocalDate jMoinsDeux = selectedDate.minusDays(2);
    LocalDate jMoinsUn = selectedDate.minusDays(1);
    List<String> centreDeCollecte = tableauDeBordOneDTO.getCentreCollectesSelected();
    List<String> typeCodeRecette = tableauDeBordOneDTO.getTypesRecettesSelected();
    List<String> devises = tableauDeBordOneDTO.getDevisesSelected();

    List<DashboardOneResponse> dashboardOneResponse = posteRepo.getReportOneData(debutMois, jMoinsDeux,
        jMoinsUn, centreDeCollecte, typeCodeRecette, devises);
    return dashboardOneResponse;


  }

  @Override
  public List<SituationConsolideeResponse> findSituationConsolidee(RapportConsolideDTO rapportConsolideDto) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<DashboardTwoResponse> findSituationPeriodique(TableauDeBordTwoDTO tableauDeBordTwoDTO) {
    System.out.println("Objet du formulaire=" + tableauDeBordTwoDTO);
    List<String> centreDeCollecte = tableauDeBordTwoDTO.getCentreCollectesSelected();
    List<String> typesRecettes = tableauDeBordTwoDTO.getTypesRecettesSelected();
    List<String> devises = tableauDeBordTwoDTO.getDevisesSelected();
    LocalDate startDate = tableauDeBordTwoDTO.getStartDate();
    LocalDate endDate = tableauDeBordTwoDTO.getEndDate();
    List<DashboardTwoResponse> dashboardTwoResponse = posteRepo.getSituationPeriodique(startDate, endDate,
        centreDeCollecte, typesRecettes, devises);
    return dashboardTwoResponse;
  }
}
