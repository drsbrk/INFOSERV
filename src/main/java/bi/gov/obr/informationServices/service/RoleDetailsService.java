package bi.gov.obr.informationServices.service;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import bi.gov.obr.informationServices.entity.RoleDetails;

public interface RoleDetailsService {
  public static final DataSource dataSource = null;



  void deleteBycol1roleIdAndcol2menuId(int roleId, int menuId);

  Optional<List<RoleDetails>> findRoleMenuDetails(int roleId, int menuId);


  public Iterable<RoleDetails> getAll();

  public Optional<List<Integer>> getAssignedRolePerRole(Integer integer);

  public Optional<List<Integer>> getAssignedSubMenusPerRoleAndMenu(Integer integer, int menu_id);

  public Optional<RoleDetails> getByPK(Integer roleDetailsPK);

  public Optional<List<Integer>> getRolesPerProfile(int role_id);

  public void saveRoleDetails(List<RoleDetails> roleDetails);

  public void saveRoleDetails(RoleDetails roleDetails);


}
