package bi.gov.obr.informationServices.service;

import java.util.List;

import bi.gov.obr.informationServices.entity.Role;

public interface RoleService {
  public Role addRole(Role role);

  public List<Role> findAll();



  public Role findRole(Integer roleCode);

  public Role findRoleByName(String roleCode);
}
