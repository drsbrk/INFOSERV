package bi.gov.obr.informationServices.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import bi.gov.obr.informationServices.dto.ReportOneProjection;
import bi.gov.obr.informationServices.utils.NativeNamedQueryDef;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "recetteobr")
@Table(name = "recetteobr")
@NamedNativeQuery(name = "ReportOne",
query = NativeNamedQueryDef.RECETTE_OBR_REPORT_ONE,
resultSetMapping = "Mapping.ReportOne")
@NamedNativeQuery(name = "ReportOne.count",
query = NativeNamedQueryDef.RECETTE_OBR_REPORT_ONE_COUNT)
@NamedNativeQuery(name = "SituationConsolide.find", query = NativeNamedQueryDef.RECETTE_OBR_SITUATION_CONSOLIDEE)
@NamedNativeQuery(name = "ReportOneWhenCentreCollecteIsEmpty", query = NativeNamedQueryDef.RECETTE_OBR_REPORT_ONE_WITH_EMPTY_CENTRE_COLLECTE, resultSetMapping = "Mapping.ReportOne")
@NamedNativeQuery(name = "ReportRecordWithinPeriodWhenCentreCollecteIsEmpty", query = NativeNamedQueryDef.RECETTE_OBR_REPORT_RECORD_WITHIN_PERIOD, resultSetMapping = "Mapping.ReportRecordsWithinPeriod")
@SqlResultSetMapping(name = "Mapping.ReportOne", classes = @ConstructorResult(targetClass = ReportOneProjection.class,
columns = { @ColumnResult(name = "code"), @ColumnResult(name = "libelle"), @ColumnResult(name = "currency"),
    @ColumnResult(name = "montant_recette_j_moins_deux"), @ColumnResult(name = "montant_recette_j_moins_un") }))
@SqlResultSetMapping(name = "Mapping.ReportRecordsWithinPeriod", classes = @ConstructorResult(targetClass = ReportOneProjection.class,
columns = { @ColumnResult(name = "code"), @ColumnResult(name = "libelle"), @ColumnResult(name = "currency"),
    @ColumnResult(name = "montant_recette_collecte") }))

@NamedNativeQuery(name = "RecetteObr.getReportTwoRecords", query = "SELECT p.code, p.libelle nomCentre, R.rtobrcdate datePaie, REPLACE(FORMAT(SUM(CASE WHEN R.rtobrcdate BETWEEN ?1 AND ?2 THEN R.rtobrcmon ELSE 0 END),0), ',',' ') as montant, R.rtobrcdev devise,\r\n"
    + "R.rtobrcnif nif, R.rtobrCNOMC nomContrib, R.rtobrcobj compte, C.libelle nomTaxe, R.rtobrcnum numQuittance from recette_obr R JOIN code_recettes C on C.code = R.rtobrcobj join poste_collecte p ON p.code = R.rtobrcstat WHERE R.rtobrcdate BETWEEN ?1 and ?2 GROUP BY R.rtobrcdev, R.rtobrcstat ORDER BY R.rtobrcstat")


@NamedNativeQuery(name = "RecetteObr.ReportOneWithListElementEmpty",
query = "SELECT p.code as code,p.libelle as libelle,r.rtobrcdev as currency,"
    + "        sum(CASE " + "            WHEN r.RTOBRCDATE BETWEEN ?1 AND ?3 THEN r.RTOBRCMON"
    + "            ELSE 0" + "        END) as montant_recette_j_moins_deux," + "        sum(CASE "
    + "            WHEN r.RTOBRCDATE BETWEEN ?1 AND ?2 THEN r.RTOBRCMON "
    + "            ELSE 0 " + "        END) as montant_recette_j_moins_un " + "    from "
    + "        poste_collecte p "
    + "    join" + "        recette_obr r " + "            on p.code=r.RTOBRCSTAT "
    + "    where"
    + "        r.RTOBRCDATE BETWEEN ?1 AND ?2        "
    + "    GROUP BY" + "        p.code,"
    + "        r.rtobrcdev LIMIT ?4,?5",
    resultSetMapping = "Mapping.ReportOneWithListElementEmpty")
@SqlResultSetMapping(name = "Mapping.ReportOneWithListElementEmpty", classes = @ConstructorResult(targetClass = ReportOneProjection.class, columns = {
    @ColumnResult(name = "code"), @ColumnResult(name = "libelle"), @ColumnResult(name = "currency"),
    @ColumnResult(name = "montant_recette_j_moins_deux"), @ColumnResult(name = "montant_recette_j_moins_un") }))

@NamedNativeQuery(name = "RecetteObr.ReportOneWith4Params", query = "", resultSetMapping = "Mapping.ReportOneWith4Params")
@SqlResultSetMapping(name = "Mapping.ReportOneWith4Params")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecetteObr {

  @Id

  /*
   * @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
   */

  @Column(name = "RTOBRCNUM", nullable = false, unique = true)
  private String numeroQuittance;
  @Column(name = "RTOBRCREC", nullable = false)
  private LocalDate dateQuittance;
  @Column(name = "RTOBRCDATE")
  private LocalDate datePaiement;
  @Column(name = "RTOBRCDEP", nullable = false)
  private String typeCodeRecette;
  @Column(name = "RTOBRCSTAT", nullable = false)
  private String centreCollecte;
  @Column(name = "RTOBRCOBJ", nullable = false)
  private String codeRecette;
  @Column(name = "RTOBRCDEV", nullable = false)
  private String deviseRecette;
  @Column(name = "RTOBRCMON", nullable = false)
  private Long montantRecette;

  @Column(name = "RTOBRCNOMC")
  private String nomContribuable;

  @Column(name = "RTOBRCPRNC")
  private String prenomContribuable;

  @Column(name = "RTOBRCNIF")
  private String nif;

  @Column(name = "RTOBRCMODP", nullable = false)
  private String modePaiement;
  @Column(name = "RTOBRCAPPRO", nullable = true)
  private String approbationRecette;
}
