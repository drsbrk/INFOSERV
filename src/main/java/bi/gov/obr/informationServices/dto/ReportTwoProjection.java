package bi.gov.obr.informationServices.dto;

import java.time.LocalDate;

import bi.gov.obr.informationServices.utils.Helper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportTwoProjection {

  private LocalDate datePaie;
  private LocalDate dateCredit;
  private String nif;
  private String nomContrib;
  private String bordereau;
  private String codeRecette;
  private String libelle;
  private Long montant;
  private String periodeFisc;
  private Long numQuittance;

  public ReportTwoProjection(LocalDate datePaie, LocalDate dateCredit, String nif, String nomContrib, String bordereau,
      String codeRecette, String libelle, Long montant, String periodeFisc, long numQuittance) {
    this.datePaie = datePaie;
    this.dateCredit = dateCredit;
    this.nif = nif;
    this.nomContrib = nomContrib;
    this.bordereau = bordereau;
    this.codeRecette = codeRecette;
    this.libelle = libelle;
    this.montant = montant;
    this.periodeFisc = periodeFisc;
    this.numQuittance = numQuittance;
  }



  public String getMontant() {
    if (montant != null) {
      return Helper.formaterMontant().format(montant);
    }
    return "0";
  }


  /*
   * public String getMontantRecetteJMoinsUnS() { if (montantRecetteJMoinsUn !=
   * null) { return Helper.formaterMontant().format(montantRecetteJMoinsUn); }
   * return "0"; }
   *
   *
   * public String getVariationS() { if (montantRecetteJMoinsUn == null) {
   * montantRecetteJMoinsUn = 0L; } if (montantRecetteJMoinsDeux == null) {
   * montantRecetteJMoinsDeux = 0L; }
   *
   * return Helper.formaterMontant().format(montantRecetteJMoinsUn -
   * montantRecetteJMoinsDeux); }
   */


}
