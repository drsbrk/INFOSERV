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
public class TableauDeBordOneDTO {

  private List<String> centreCollectesSelected = new ArrayList<>();
  private List<String> typesRecettesSelected = new ArrayList<>();
  private List<String> devisesSelected = new ArrayList<>();
  private LocalDate selectedDate;

  private List<PosteCollecte> centreCollectes = new ArrayList<>();
  private List<TypeCodeRecette> typeCodeRecettes = new ArrayList<>();
  private List<Devise> devises = new ArrayList<>();

  private int page;
  private int size;
}
