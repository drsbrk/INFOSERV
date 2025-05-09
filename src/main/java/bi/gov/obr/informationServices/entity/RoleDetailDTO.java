package bi.gov.obr.informationServices.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class RoleDetailDTO {
  private int role_id;
  private int menu_id;
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
    RoleDetailDTO other = (RoleDetailDTO) obj;
    if (menu_id != other.menu_id) {
      return false;
    }
    if (role_id != other.role_id) {
      return false;
    }
    return true;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + menu_id;
    result = prime * result + role_id;
    return result;
  }
  @Override
  public String toString() {
    return "RoleDetailDTO [roleId=" + role_id + ", menuId=" + menu_id + "]";
  }
}
