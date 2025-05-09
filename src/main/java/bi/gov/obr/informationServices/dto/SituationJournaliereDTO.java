package bi.gov.obr.informationServices.dto;

import bi.gov.obr.informationServices.utils.Helper;

//This purpose of this class is to well display the amount out of the DB.
//The amount to display is for the report 4
public class SituationJournaliereDTO {

  private String croissance_CDA;
  private String croissance_CTI;
  private String croissance_Mensuelle_CDA;
  private String ecart_Mensuel_Realisation_Prevision_CDA;
  private String ecart_Mensuel_Realisation_Prevision_CTI;
  private String ecart_Mensuel_Total;
  private String pourcentage_Realisation_Mensuelle_CDA;
  private String pourcentage_Realisation_Mensuelle_CTI;
  private String pourcentage_Realisation_Mensuelle_OBR;
  private String pourcentage_Realisation_Mensuelle_Totale;
  private String previsionMensuelleCDA;
  private String prevision_Mensuelle_CTI;
  private String prevision_Mensuelle_Totale;
  private String realisation_MensuelleAnprecCDA;
  private String realisation_Mensuelle_Anprec_CTI;
  private String realisation_Mensuelle_CDA;
  private String realisation_Mensuelle_CTI;
  private String realisation_Mensuelle_Totale;
  private SituationConsolideeResponse situationConsolideeResponse;

  public String getCroissance_CDA() {

    if (situationConsolideeResponse.getCroissance_CDA() != null && situationConsolideeResponse.getCroissance_CDA() != null) {
      croissance_CDA = Helper.formaterMontant().format(situationConsolideeResponse.getCroissance_CDA());
    } else {
      croissance_CDA = "N/A";
    }

    return croissance_CDA;
  }

  public String getCroissance_CTI() {

    if (situationConsolideeResponse.getCroissance_CTI() != null && situationConsolideeResponse.getCroissance_CTI() != null) {
      croissance_CTI = Helper.formaterMontant().format(situationConsolideeResponse.getCroissance_CTI());
    } else {
      croissance_CTI = "N/A";
    }

    return croissance_CTI;

  }

  public String getCroissance_Mensuelle_CDA() {

    if (situationConsolideeResponse.getCroissance_CDA() != null
        && situationConsolideeResponse.getCroissance_CDA() != null) {
      croissance_CDA = Helper.formaterMontant().format(situationConsolideeResponse.getCroissance_CDA());
    } else {
      croissance_CDA = "N/A";
    }
    return croissance_CDA;
  }

  public String getEcart_Mensuel_Realisation_Prevision_CDA() {

    if (situationConsolideeResponse.getEcart_Mensuel_Realisation_Prevision_CDA() != null && situationConsolideeResponse.getEcart_Mensuel_Realisation_Prevision_CDA() != null) {
      ecart_Mensuel_Realisation_Prevision_CDA = Helper.formaterMontant().format(situationConsolideeResponse.getEcart_Mensuel_Realisation_Prevision_CDA());
    } else {
      ecart_Mensuel_Realisation_Prevision_CDA = "N/A";
    }

    return ecart_Mensuel_Realisation_Prevision_CDA;
  }

  public String getEcart_Mensuel_Realisation_Prevision_CTI() {

    if (situationConsolideeResponse.getEcart_Mensuel_Realisation_Prevision_CTI() != null && situationConsolideeResponse.getEcart_Mensuel_Realisation_Prevision_CTI() != null) {
      ecart_Mensuel_Realisation_Prevision_CTI = Helper.formaterMontant().format(situationConsolideeResponse.getEcart_Mensuel_Realisation_Prevision_CTI());
    } else {
      ecart_Mensuel_Realisation_Prevision_CTI = "N/A";
    }

    return ecart_Mensuel_Realisation_Prevision_CTI;
  }

  public String getEcart_Mensuel_Total() {

    if (situationConsolideeResponse.getEcart_Mensuel_Total() != null && situationConsolideeResponse.getEcart_Mensuel_Total() != null) {
      ecart_Mensuel_Total = Helper.formaterMontant().format(situationConsolideeResponse.getEcart_Mensuel_Total());
    } else {
      ecart_Mensuel_Total = "N/A";
    }

    return ecart_Mensuel_Total;
  }

  public String getPourcentage_Realisation_Mensuelle_CDA() {

    if (situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_CTI() != null && situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_CTI() != null) {
      pourcentage_Realisation_Mensuelle_CTI = Helper.formaterMontant().format(situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_CTI());
    } else {
      pourcentage_Realisation_Mensuelle_CTI = "N/A";
    }

    return pourcentage_Realisation_Mensuelle_CTI;
  }

  public String getPourcentage_Realisation_Mensuelle_CTI() {

    if (situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_CTI() != null && situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_CTI() != null) {
      pourcentage_Realisation_Mensuelle_CTI = Helper.formaterMontant().format(situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_CTI());
    } else {
      pourcentage_Realisation_Mensuelle_CTI = "N/A";
    }

    return pourcentage_Realisation_Mensuelle_CTI;
  }

  public String getPourcentage_Realisation_Mensuelle_OBR() {

    if (situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_OBR() != null && situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_OBR() != null) {
      pourcentage_Realisation_Mensuelle_OBR = Helper.formaterMontant().format(situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_OBR());
    } else {
      pourcentage_Realisation_Mensuelle_OBR = "N/A";
    }

    return pourcentage_Realisation_Mensuelle_OBR;
  }

  public String getPourcentage_Realisation_Mensuelle_Totale() {

    if (situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_Totale() != null && situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_Totale() != null) {
      pourcentage_Realisation_Mensuelle_Totale = Helper.formaterMontant().format(situationConsolideeResponse.getPourcentage_Realisation_Mensuelle_Totale());
    } else {
      pourcentage_Realisation_Mensuelle_Totale = "N/A";
    }

    return pourcentage_Realisation_Mensuelle_Totale;
  }

  public String getPrevision_Mensuelle_CTI() {

    if (situationConsolideeResponse.getPrevision_Mensuelle_CTI() != null && situationConsolideeResponse.getPrevision_Mensuelle_CTI() != null) {
      prevision_Mensuelle_CTI = Helper.formaterMontant().format(situationConsolideeResponse.getPrevision_Mensuelle_CTI());
    } else {
      prevision_Mensuelle_CTI = "N/A";
    }

    return prevision_Mensuelle_CTI;
  }

  public String getPrevision_Mensuelle_Totale() {

    if (situationConsolideeResponse.getPrevision_Mensuelle_Totale() != null && situationConsolideeResponse.getPrevision_Mensuelle_Totale() != null) {
      prevision_Mensuelle_Totale = Helper.formaterMontant().format(situationConsolideeResponse.getPrevision_Mensuelle_Totale());
    } else {
      prevision_Mensuelle_Totale = "N/A";
    }

    return prevision_Mensuelle_Totale;
  }

  public String getPrevisionMensuelleCDA() {

    if (situationConsolideeResponse.getPrevision_Mensuelle_CDA() != null && situationConsolideeResponse.getPrevision_Mensuelle_CDA() != null) {
      previsionMensuelleCDA = Helper.formaterMontant().format(situationConsolideeResponse.getPrevision_Mensuelle_CDA());
    } else {
      previsionMensuelleCDA = "N/A";
    }

    return previsionMensuelleCDA;
  }

  public String getRealisation_Mensuelle_Anprec_CTI() {
    if (situationConsolideeResponse.getRealisation_Mensuelle_Anprec_CTI() != null && situationConsolideeResponse.getRealisation_Mensuelle_Anprec_CTI() != null) {
      realisation_Mensuelle_Anprec_CTI = Helper.formaterMontant().format(situationConsolideeResponse.getRealisation_Mensuelle_Anprec_CTI());
    } else {
      realisation_Mensuelle_Anprec_CTI = "N/A";
    }
    return realisation_Mensuelle_Anprec_CTI;
  }

  public String getRealisation_Mensuelle_CDA() {
    if (situationConsolideeResponse.getRealisation_Mensuelle_CDA() != null && situationConsolideeResponse.getRealisation_Mensuelle_CDA() != null) {
      realisation_Mensuelle_CDA = Helper.formaterMontant().format(situationConsolideeResponse.getRealisation_Mensuelle_CDA());
    } else {
      realisation_Mensuelle_CDA = "N/A";
    }
    return realisation_Mensuelle_CDA;
  }

  public String getRealisation_Mensuelle_CTI() {
    if (situationConsolideeResponse.getRealisation_Mensuelle_CTI() != null && situationConsolideeResponse.getRealisation_Mensuelle_CTI() != null) {
      realisation_Mensuelle_CTI = Helper.formaterMontant().format(situationConsolideeResponse.getRealisation_Mensuelle_CTI());
    } else {
      realisation_Mensuelle_CTI = "N/A";
    }
    return realisation_Mensuelle_CTI;
  }

  public String getRealisation_Mensuelle_Totale() {
    if (situationConsolideeResponse.getRealisation_Mensuelle_Totale() != null && situationConsolideeResponse.getRealisation_Mensuelle_Totale() != null) {
      realisation_Mensuelle_Totale = Helper.formaterMontant().format(situationConsolideeResponse.getRealisation_Mensuelle_Totale());
    } else {
      realisation_Mensuelle_Totale = "N/A";
    }
    return realisation_Mensuelle_Totale;
  }

  public String getRealisation_MensuelleAnprecCDA() {
    if (situationConsolideeResponse.getRealisation_Mensuelle_Anprec_CDA() != null
        && situationConsolideeResponse.getRealisation_Mensuelle_Anprec_CDA() != null) {
      realisation_MensuelleAnprecCDA = Helper.formaterMontant()
          .format(situationConsolideeResponse.getRealisation_Mensuelle_Anprec_CDA());
    } else {
      realisation_MensuelleAnprecCDA = "N/A";
    }
    return realisation_MensuelleAnprecCDA;
  }

  public SituationConsolideeResponse getSituationConsolideeResponse() {
    return situationConsolideeResponse;
  }

  public void setSituationConsolideeResponse(SituationConsolideeResponse situationConsolideeResponse) {
    this.situationConsolideeResponse = situationConsolideeResponse;
  }
}
