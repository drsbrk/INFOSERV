package bi.gov.obr.informationServices.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportTwo {
  // periode_fiscal, date_credit are omitted in this version because in this
  // version there are no BRB interfaces
  private String code;
  private String nomCentre;
  private String devise;
  private LocalDate dapePaie;
  private String nif;
  private String nomContrib;
  // private String bordereau = "";
  private String codeRecette;
  private String nomTaxe;
  private Long montant;
  private Long numQuittance;



  public Long getMontant() {
    return montant;
  }

  public void setMontant(Long montant) {
    this.montant = montant;
  }

}
