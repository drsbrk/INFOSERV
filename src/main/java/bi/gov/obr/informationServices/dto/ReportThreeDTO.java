package bi.gov.obr.informationServices.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.entity.Exercice;
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
public class ReportThreeDTO {
  private List<PosteCollecte> centreDeCollectes = new ArrayList<>();
  private List<CompteImpot> compteImpot = new ArrayList<>();
  private List<Exercice> exercices = new ArrayList<>();
  private List<TypeCodeRecette> typesRecettes = new ArrayList<>();
  private List<Devise> devises = new ArrayList<>();
  private List<String> centreDeCollectesSelected;
  private List<String> selectedTypeRecette;
  private List<String> selectedDevises;
  private List<String> compteImpotSelected;
  private List<String> exerciceSelected;
  private LocalDate startingDate;
  private LocalDate endingDate;
  private int pageIndex;
  private int size; // nber of elements on a page

}
