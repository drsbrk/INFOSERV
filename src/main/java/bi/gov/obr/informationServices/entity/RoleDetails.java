package bi.gov.obr.informationServices.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DETAILSROLES")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private int menu_id;

  private int role_id;

  private int subMenuId;

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
    RoleDetails other = (RoleDetails) obj;
    if (id != other.id) {
      return false;
    }
    if (menu_id != other.menu_id) {
      return false;
    }
    if (role_id != other.role_id) {
      return false;
    }
    if (subMenuId != other.subMenuId) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    result = prime * result + menu_id;
    result = prime * result + role_id;
    result = prime * result + subMenuId;
    return result;
  }

  @Override
  public String toString() {
    return "RoleDetails [id=" + id + ", menu_id=" + menu_id + ", role_id=" + role_id + ", subMenuId=" + subMenuId + "]";
  }
}
