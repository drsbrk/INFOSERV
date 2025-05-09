package bi.gov.obr.informationServices.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bi.gov.obr.informationServices.entity.PosteCollecte;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportTwoForm {

  private List<PosteCollecte> postesDeCollecte = new ArrayList<>();
  private List<String> typesRecettes = new ArrayList<>();
  private LocalDate startingDate;
  private LocalDate endDate;
  private List<String> typeRecettesSelected = new ArrayList<>();
  private List<String> codeRecettesSelected = new ArrayList<>();
  private List<String> devisesSelected = new ArrayList<>();
  private List<String> centreCollectesSelected = new ArrayList<>();
  /*
   * private int page; private int size;
   */
}
