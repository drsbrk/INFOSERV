package bi.gov.obr.informationServices.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "codeRecettes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CompteImpot {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  //Code utilisé dans RCMS
  private String code;
  private String libelle;
  private String typeRecette;
  //Ce code est sur 7 chiffre
  private String codePrevision;
  //Code utilisE dans ASYCUDA
  private String codeBudget;

  public CompteImpot(String code) {
    this.code = code;
  }
  public CompteImpot(String code, String typeRecette) {
    this.code = code;
    this.typeRecette = typeRecette;
  }

  public CompteImpot(String code,String libelle, String typeRecette) {
    this.code = code;
    this.typeRecette = typeRecette;
    this.libelle = libelle;
  }


  @Override
  public String toString() {
    return "CompteImpot{" +
        "id=" + id +
        ", code='" + code + '\'' +
        ", libelle='" + libelle + '\'' +
        ", typeRecette=" + typeRecette +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CompteImpot that = (CompteImpot) o;
    return Objects.equals(code, that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(code);
  }
}
