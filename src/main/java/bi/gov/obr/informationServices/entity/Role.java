
package bi.gov.obr.informationServices.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity

@Data

@AllArgsConstructor

@NoArgsConstructor

@Table(name = "roles")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @Column(nullable = false, unique = true)

  @javax.validation.constraints.NotEmpty(message = "Veuillez donner un nom au rôle")
  private String name;

  @OneToMany(mappedBy = "role")
  @JsonIgnore
  private List<User> users;

  @javax.validation.constraints.NotEmpty(message = "Veuillez attribuer un nom à ce rôle")
  private String roleName;

  @ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.EAGER)

  @JoinTable(name = "roles_menus", joinColumns = {

      @JoinColumn(referencedColumnName = "id", name = "role_id") }, inverseJoinColumns = {

          @JoinColumn(name = "menu_id", referencedColumnName = "id") })

  @NotEmpty(message = "Veuillez octroyer au moins un menu")

  @EqualsAndHashCode.Exclude

  @ToString.Exclude

  private List<MainMenu> menus = new ArrayList<>();

  public Role(Integer id) {
    this.id = id;
  }

  public Role(String name) {
    this.name = name;
  }

  public Role(String name, String roleName) {
    super();
    this.name = name;
    this.roleName = roleName;
  }



  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Role other = (Role) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "Role [id=" + id + ", name=" + name + ", roleName=" + roleName + "]";
  }

}

