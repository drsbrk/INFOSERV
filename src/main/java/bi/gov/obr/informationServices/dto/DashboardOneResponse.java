package bi.gov.obr.informationServices.dto;

import java.math.BigDecimal;

public interface DashboardOneResponse {

  String getCodesCentreDeCollecte();
  String getCurrency();

  String getLibelle();

  BigDecimal getMontant_recette_j_moins_deux();

  BigDecimal getMontant_recette_j_moins_un();
}
