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

@Data

@AllArgsConstructor
@NoArgsConstructor
public class ReportOneForm {

  private List<String> centreCollectesSelected = new ArrayList<>();
  private List<PosteCollecte> centreCollectes = new ArrayList<>();
  private LocalDate selectedDate;
  private List<TypeCodeRecette> typesRecettes = new ArrayList<>();
  private List<Devise> devises = new ArrayList<>();
  private List<String> typeRecettesSelected = new ArrayList<>();
  private List<String> codeRecettesSelected = new ArrayList<>();
  private List<String> devisesSelected = new ArrayList<>();
  private int page;
  private int size;
}
