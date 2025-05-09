package bi.gov.obr.informationServices.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDetailsDTO {
  private int roleId;
  private int menuId;
  private List<Integer> subMenuList = new ArrayList<>();
}
