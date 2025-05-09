package bi.gov.obr.informationServices.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "sousmenu")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class SubMenu {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotBlank(message = "Le nom du sous menu est obligatoire")
  private String displayName;

  private String icon;
  @NotBlank(message = "Le lien est obligatoire")
  @Column(nullable = false, unique = true)
  private String uri;
  @Column(name = "creationDate", nullable = false)
  private String creationDate;
  private String createdBy;

  @ManyToMany(mappedBy = "subMenus", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JsonIgnore

  private Set<MainMenu> parentMenus;


}
