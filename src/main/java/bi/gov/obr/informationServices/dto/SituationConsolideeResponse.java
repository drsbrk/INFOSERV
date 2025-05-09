package bi.gov.obr.informationServices.dto;

public interface SituationConsolideeResponse {
  Long getCroissance_CDA();

  Long getCroissance_CTI();

  Long getCroissanceTotale();

  Long getEcart_Mensuel_Realisation_Prevision_CDA();

  Long getEcart_Mensuel_Realisation_Prevision_CTI();

  Long getEcart_Mensuel_Total();

  Float getPourcentage_Realisation_Mensuelle_CDA();
  Float getPourcentage_Realisation_Mensuelle_CTI();

  Float getPourcentage_Realisation_Mensuelle_OBR();
  Float getPourcentage_Realisation_Mensuelle_Totale();
  Long getPrevision_Mensuelle_CDA();
  Long getPrevision_Mensuelle_CTI();

  Long getPrevision_Mensuelle_Totale();

  Long getRealisation_Mensuelle_Anprec_CDA();

  Long getRealisation_Mensuelle_Anprec_CTI();

  // CDA
  Long getRealisation_Mensuelle_CDA();

  Long getRealisation_Mensuelle_CTI();

  // TOTAUX
  Long getRealisation_Mensuelle_Totale();


}
