package bi.gov.obr.informationServices.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "menus")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class MainMenu implements Comparable<MainMenu> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, unique = true)
  @NotBlank(message = "Nom obligatoire")
  private String name;
  @Column(nullable = true)
  private String createdBy;
  @Column(nullable = false)
  private String icon;
  @Column(nullable = true)
  private String uri;
  @Column(nullable = false)
  private String creationDate;
  @ManyToMany(fetch = FetchType.EAGER)
  @NotEmpty(message = "Veuillez ajouter au moins un sous menu")
  @JoinTable(name = "mainmenu_submenus", joinColumns = {

      @JoinColumn(referencedColumnName = "id", name = "mainmenu_id") }, inverseJoinColumns = {

          @JoinColumn(name = "submenu_id", referencedColumnName = "id") })
  @EqualsAndHashCode.Exclude
  @ToString.Exclude

  private Set<SubMenu> subMenus = new HashSet<>();

  public MainMenu(Integer id, String name, String createdBy) {
    this.id = id;
    this.name = name;
    this.createdBy = createdBy;
  }

  @Override
  public int compareTo(MainMenu anotherString) {
    return this.getName().compareTo(anotherString.getName());
  }




  @Override
  public String toString() {
    return "MainMenu [id=" + id + ", name=" + name + ", createdBy=" + createdBy + "]";
  }
}
