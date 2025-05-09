package bi.gov.obr.informationServices.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import bi.gov.obr.informationServices.utils.NativeNamedQueryDef;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Entity
@Getter
@Setter
@Table(name = "postecollecte", indexes = { @Index(columnList = "code", name = "idx_poste_code") })
@NamedNativeQuery(name = "PosteCollecte.getAllPosteCollecteWithRecetteObr", query = NativeNamedQueryDef.RECETTE_OBR_REPORT_ONE_WITH_EMPTY_CENTRE_COLLECTE_COUNT)
// Added for report two
// Request posteCollecte joining
@NamedNativeQuery(name = "PosteCollecte.getAllPosteCollecteWithTypeRecetteObr", query = NativeNamedQueryDef.RECETTE_OBR_REPORT_TWO_WITH_EMPTY_CENTRE_COLLECTE_COUNT)

@NamedNativeQuery(name = "PosteCollecte.getReportOneData", query = NativeNamedQueryDef.TABLEAU_DE_BORD_REPORT_ONE_CODE_WITH_CORRESPONDING_RECETTE)
@NamedNativeQuery(name = "PosteCollecte.getSituationPeriodique", query = NativeNamedQueryDef.TABLEAU_DE_BORD_REPORT_TWO)
public class PosteCollecte {

  @Id
  @NotEmpty(message = "Le code est obligatoire")
  private String code;
  @NotEmpty(message = "Le libellé est obligatoire")
  private String libelle;


}
