package bi.gov.obr.informationServices.service;

import java.util.List;

import javax.validation.Valid;

import bi.gov.obr.informationServices.entity.SubMenu;

public interface SubMenuService {
  public List<SubMenu> findAll();

  public SubMenu findSubMenuByID(int id);

  public SubMenu save(@Valid SubMenu subMenu);
}
