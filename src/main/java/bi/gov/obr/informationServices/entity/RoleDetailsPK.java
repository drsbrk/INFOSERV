package bi.gov.obr.informationServices.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleDetailsPK implements Serializable {
  private final static long serialVersionId = 1L;
  private int col1RoleId;
  private int col2MenuId;
  private int col3SubMenuId;
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
    RoleDetailsPK other = (RoleDetailsPK) obj;
    if (col1RoleId != other.col1RoleId) {
      return false;
    }
    if (col2MenuId != other.col2MenuId) {
      return false;
    }
    if (col3SubMenuId != other.col3SubMenuId) {
      return false;
    }
    return true;
  }
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + col1RoleId;
    result = prime * result + col2MenuId;
    result = prime * result + col3SubMenuId;
    return result;
  }
}
