package bi.gov.obr.informationServices.utils;

public class NativeNamedQueryDef {

  public static final String TABLEAU_DE_BORD_REPORT_ONE_CODE_WITH_CORRESPONDING_RECETTE = "SELECT  p.code as codesCentreDeCollecte, p.libelle as libelle,  sum(CASE WHEN r.RTOBRCDATE BETWEEN ?1 AND ?2 THEN r.RTOBRCMON ELSE 0 END) as montant_recette_j_moins_deux, sum(CASE WHEN r.RTOBRCDATE BETWEEN ?1 AND ?3 THEN r.RTOBRCMON ELSE 0 END) as montant_recette_j_moins_un, r.rtobrcdev as currency from postecollecte p join recetteobr r on p.code=r.RTOBRCSTAT where r.RTOBRCDATE BETWEEN ?1 AND ?3 \r\n"
      + "AND (COALESCE(?4, NULL) IS NULL OR r.RTOBRCSTAT IN ?4 )  AND (COALESCE(?5, NULL) IS NULL OR r.RTOBRCDEP IN ?5 )    \r\n"
      + "AND (COALESCE(?6, NULL) IS NULL OR r.RTOBRCDEV IN ?6 ) GROUP BY p.code,r.rtobrcdev";
  public static final String RECETTE_OBR_SITUATION_CONSOLIDEE = " SELECT robr.*,ROUND((realisation_mensuelle_CTI+realisation_mensuelle_CDA)*100/(prevision_mensuelle_CTI+prevision_mensuelle_CDA),2) as pourcentage_realisation_mensuelle_OBR,ROUND((realisation_mensuelle_CDA*100)/prevision_mensuelle_CDA,2) as pourcentage_realisation_mensuelle_CDA,robr.realisation_mensuelle_CDA-robr.prevision_mensuelle_CDA as ecart_mensuel_realisation_prevision_CDA,robr.realisation_mensuelle_CTI+robr.realisation_mensuelle_CDA as realisation_mensuelle_totale,robr.prevision_mensuelle_CTI+robr.prevision_mensuelle_CDA as prevision_mensuelle_totale,ROUND((robr.realisation_mensuelle_CTI+robr.realisation_mensuelle_CDA)*100/(robr.prevision_mensuelle_CTI+robr.prevision_mensuelle_CDA),2) as pourcentage_realisation_mensuelle_totale,(robr.realisation_mensuelle_CTI+robr.realisation_mensuelle_CDA)-(robr.prevision_mensuelle_CTI+robr.prevision_mensuelle_CDA) as ecart_mensuel_total,robr.realisation_mensuelle_CTI-robr.realisation_mensuelle_anprec_CTI as croissance_CTI,robr.realisation_mensuelle_CDA-robr.realisation_mensuelle_anprec_CDA  as croissance_CDA,((robr.realisation_mensuelle_CTI-robr.realisation_mensuelle_anprec_CTI)+(robr.realisation_mensuelle_CDA-robr.realisation_mensuelle_anprec_CDA)) as croissanceTotale FROM (SELECT b.*,ROUND(b.realisation_mensuelle_CTI*100/b.prevision_mensuelle_CTI,2) as pourcentage_realisation_mensuelle_CTI FROM (SELECT a.*,"
      + " a.realisation_mensuelle_CTI - a.prevision_mensuelle_CTI as ecart_mensuel_realisation_prevision_CTI FROM "
      + "(SELECT (SELECT SUM(CASE WHEN r.RTOBRCDEV = 'US $' THEN  ( SELECT colltest.taux_de_change.taux_du_jour FROM colltest.taux_de_change WHERE colltest.taux_de_change.created = r.RTOBRCDATE)  * r.RTOBRCMON WHEN r.RTOBRCDEV = 'BIF' THEN r.RTOBRCMON"
      + " ELSE  r.RTOBRCMON" + " END )"
      + " FROM recetteobr r LEFT JOIN colltest.taux_de_change t ON r.RTOBRCDATE = t.created WHERE r.RTOBRCDATE BETWEEN ?2 AND ?3  AND r.RTOBRCDEP IN ( '01')) as realisation_mensuelle_CTI,"
      + " (SELECT SUM(CASE WHEN  p.montant_revise_cti IS NULL THEN p.montant_cti ELSE p.montant_revise_cti END) FROM prevision p join code_recettes c  on p.compte_id = c.id WHERE exercice_id = ?1 AND prevision_month = ?6 ) as  prevision_mensuelle_CTI, (SELECT SUM(CASE WHEN  p.montant_revise_cda IS NULL THEN p.montant_cda ELSE p.montant_revise_cda END)"
      + " FROM prevision p join code_recettes c on p.compte_id = c.id WHERE exercice_id = ?1 AND prevision_month= ?6 ) as prevision_mensuelle_CDA,"
      + " (SELECT SUM(CASE WHEN r.RTOBRCDEV = 'US $' THEN  ( SELECT colltest.taux_de_change.taux_du_jour FROM colltest.taux_de_change WHERE colltest.taux_de_change.created = r.RTOBRCDATE)  * r.RTOBRCMON "
      + " WHEN r.RTOBRCDEV = 'BIF' THEN r.RTOBRCMON "
      + " ELSE  r.RTOBRCMON END) FROM recetteobr r WHERE r.RTOBRCDATE BETWEEN ?2 AND ?3 AND r.RTOBRCDEP IN('02')) as realisation_mensuelle_CDA,"
      + " (SELECT SUM(CASE WHEN r.RTOBRCDEV = 'US $' THEN  ( SELECT colltest.taux_de_change.taux_du_jour FROM colltest.taux_de_change WHERE colltest.taux_de_change.created = r.RTOBRCDATE)  * r.RTOBRCMON "
      + "  WHEN r.RTOBRCDEV = 'BIF' THEN r.RTOBRCMON ELSE  r.RTOBRCMON END) FROM recetteobr r WHERE r.RTOBRCDATE BETWEEN ?4 AND ?5 AND r.RTOBRCDEP IN ('01')) as realisation_mensuelle_anprec_CTI,"
      + " (SELECT SUM(CASE WHEN r.RTOBRCDEV = 'US $' THEN  ( SELECT colltest.taux_de_change.taux_du_jour FROM colltest.taux_de_change WHERE colltest.taux_de_change.created = r.RTOBRCDATE)  * r.RTOBRCMON "
      + " WHEN r.RTOBRCDEV = 'BIF' THEN r.RTOBRCMON ELSE  r.RTOBRCMON END) FROM recetteobr r WHERE r.RTOBRCDATE BETWEEN ?4 AND ?5 AND r.RTOBRCDEP IN ('02')) as realisation_mensuelle_anprec_CDA) a) b) robr";
  public static final String TABLEAU_DE_BORD_REPORT_TWO = "SELECT p.code as code, p.libelle as nomCentre, r.rtobrcdev as devise, "
      + "      SUM(CASE WHEN r.rtobrcdate BETWEEN ?1 AND  ?2 THEN r.rtobrcmon ELSE 0 END) as montant "
      + "      FROM postecollecte p   join recetteobr r on p.code = r.rtobrcstat WHERE "
      + "      ( r.RTOBRCDATE BETWEEN ?1 AND ?2 " + "      AND"
      + "      (COALESCE(?3, NULL) IS NULL OR r.RTOBRCSTAT IN ?3) AND (COALESCE(?4, NULL) IS NULL OR r.RTOBRCDEP IN ?4) AND (COALESCE(?5, NULL) IS NULL OR r.RTOBRCDEV IN ?5)) GROUP BY p.code,r.rtobrcdev ORDER BY montant DESC LIMIT 5";

  public static final String RECETTE_OBR_REPORT_ONE = "SELECT p.code as code, p.libelle as libelle,r.rtobrcdev as currency, sum(CASE WHEN r.RTOBRCDATE BETWEEN ?1 AND ?3 THEN r.RTOBRCMON ELSE 0 END) "
      + "as montant_recette_j_moins_deux, " + "sum(CASE WHEN r.RTOBRCDATE BETWEEN ?1 "
      + "AND ?2 THEN r.RTOBRCMON ELSE 0 END) as montant_recette_j_moins_un from postecollecte p join recetteobr r on p.code=r.RTOBRCSTAT where r.RTOBRCDATE BETWEEN ?1 AND ?2 "
      + "AND r.rtobrcstat IN ?4 GROUP BY p.code,r.rtobrcdev";


  public static final String RECETTE_OBR_REPORT_ONE_COUNT = "select count(*) from postecollecte p join recetteobr r on p.code=r.RTOBRCSTAT where r.RTOBRCDATE BETWEEN ?1 AND ?2 GROUP BY p.code,r.rtobrcdev";
  public static final String RECETTE_OBR_REPORT_ONE_WITH_EMPTY_CENTRE_COLLECTE_COUNT = "select count(*) as totalElements from postecollecte p join recetteobr r on p.code=r.RTOBRCSTAT where r.RTOBRCDATE BETWEEN ?1 AND ?2 AND (COALESCE(?3, NULL) IS NULL OR r.RTOBRCSTAT IN ?3) AND (COALESCE(?4, NULL) IS NULL OR r.RTOBRCDEP IN ?4) AND (COALESCE(?5, NULL) IS NULL OR r.RTOBRCDEV IN ?5) group by p.code,r.RTOBRCDEV";
  public static final String RECETTE_OBR_REPORT_TWO_WITH_EMPTY_CENTRE_COLLECTE_COUNT = "select count(*) as totalElements from postecollecte p join recetteobr r on p.code=r.RTOBRCSTAT where r.RTOBRCDATE BETWEEN ?1 AND ?2 AND (COALESCE(?3, NULL) IS NULL OR r.RTOBRCSTAT IN ?3) AND (COALESCE(?4, NULL) IS NULL OR r.RTOBRCDEP IN ?4) AND (COALESCE(?5, NULL) IS NULL OR r.RTOBRCDEV IN ?5) group by p.code,r.RTOBRCDEV";
  public static final String RECETTE_OBR_REPORT_ONE_WITH_EMPTY_CENTRE_COLLECTE = "SELECT p.code as code, p.libelle as libelle,r.rtobrcdev as currency, "
      + "sum(CASE WHEN r.RTOBRCDATE BETWEEN ?1 AND ?3 THEN r.RTOBRCMON ELSE 0 END) "
      + "as montant_recette_j_moins_deux, "
      + "sum(CASE WHEN r.RTOBRCDATE BETWEEN ?1 AND ?2 THEN r.RTOBRCMON ELSE 0 END) " + "as montant_recette_j_moins_un "
      + "from postecollecte p join recetteobr r on p.code=r.RTOBRCSTAT "
      + "where (r.RTOBRCDATE BETWEEN ?1 AND ?2 AND (COALESCE(?4, NULL) IS NULL OR r.RTOBRCSTAT IN ?4) AND (COALESCE(?5, NULL) IS NULL OR r.RTOBRCDEP IN ?5) AND (COALESCE(?6, NULL) IS NULL OR r.RTOBRCDEV IN ?6)) GROUP BY p.code,r.RTOBRCDEV limit ?7,?8";

  public static final String RECETTE_OBR_REPORT_RECORD_WITHIN_PERIOD = "SELECT p.code as code, p.libelle as libelle,r.rtobrcdev as currency, "
      + "sum(CASE WHEN r.RTOBRCDATE BETWEEN ?1 AND ?2 THEN r.RTOBRCMON ELSE 0 END) " + "as montant_recette_collecte "
      + "from postecollecte p join recetteobr r on p.code=r.RTOBRCSTAT "
      + "where (r.RTOBRCDATE BETWEEN ?1 AND ?2 AND (COALESCE(?3, NULL) IS NULL OR r.RTOBRCSTAT IN ?3) AND (COALESCE(?4, NULL) IS NULL OR r.RTOBRCDEP IN ?4) AND (COALESCE(?5, NULL) IS NULL OR r.RTOBRCDEV IN ?5)) GROUP BY p.code,r.RTOBRCDEV limit ?6,?7";

}
