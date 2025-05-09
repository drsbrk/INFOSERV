package bi.gov.obr.informationServices.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bi.gov.obr.informationServices.entity.PosteCollecte;
import bi.gov.obr.informationServices.enums.Devise;
import bi.gov.obr.informationServices.enums.TypeCodeRecette;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReportTwoDTO {
  private List<String> centreDeCollectesSelected = new ArrayList<>();
  private List<PosteCollecte> centreDeCollectes = new ArrayList<>();
  private List<TypeCodeRecette> typesRecettes = new ArrayList<>();
  private List<String> selectedTypeRecette = new ArrayList<>();
  private List<String> codeRecettesSelected = new ArrayList<>();
  private List<Devise> devises = new ArrayList<>();
  private List<String> devisesSelected = new ArrayList<>();
//  private List<String> selectedDevises = new ArrayList<>();
  private LocalDate startingDate;
  private LocalDate endingDate;
  private int pageIndex;
  private int size; // nber of elements on a page

}
