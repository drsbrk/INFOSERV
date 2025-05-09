package bi.gov.obr.informationServices.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.entity.RoleDetails;
import bi.gov.obr.informationServices.repository.CompositePKRoleDetailsRepository;

@Service
public class RoleDetailsServiceImpl implements RoleDetailsService {
  @Autowired
  CompositePKRoleDetailsRepository compositePKRoleDetailsRepository;
  @Autowired
  MainMenuService mainMenuService;
  @Autowired
  RoleService roleService;

  @Override
  public void deleteBycol1roleIdAndcol2menuId(int roleId, int menuId) {
    compositePKRoleDetailsRepository.deleteBycol1roleIdAndcol2menuId(roleId, menuId);
  }


  @Override
  public Optional<List<RoleDetails>> findRoleMenuDetails(int roleId, int menuId) {
    // TODO Auto-generated method stub
    try {
      if (roleId != 0 && menuId != 0) {
        Optional<List<RoleDetails>> listRoleDetails = compositePKRoleDetailsRepository.detailsByRole_idAndMenu_id(roleId, menuId);
        if(listRoleDetails != null) {
          return listRoleDetails;
        }else {
          return null;
        }
      }
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  @Override
  public Iterable<RoleDetails> getAll() {
    return compositePKRoleDetailsRepository.findAll();
  }

  @Override
  // Cette methode return une liste d'Id de menus accordés à l'utilisateur
  public Optional<List<Integer>> getAssignedRolePerRole(Integer role_Id) {
    Optional<List<Integer>> assignedRoles = compositePKRoleDetailsRepository.getProfileRoleDetails(role_Id);
    if (assignedRoles.isPresent()) {
      return assignedRoles;
    }
    return null;
  }

  @Override
  public Optional<List<Integer>> getAssignedSubMenusPerRoleAndMenu(Integer role_id, int menu_id) {
    Optional<List<Integer>> subMenus = compositePKRoleDetailsRepository.getAssignedSubMenusPerRoleAndMenu(role_id,
        menu_id);
    return Optional.ofNullable(subMenus.get());
  }

  @Override
  public Optional<RoleDetails> getByPK(Integer roleDetailsPK) {

    return compositePKRoleDetailsRepository.findById(roleDetailsPK);
  }

  @Override
  public Optional<List<Integer>> getRolesPerProfile(int role_id) {
    return null;
  }

  @Override
  public void saveRoleDetails(List<RoleDetails> roleDetails) {
    compositePKRoleDetailsRepository.saveAll(roleDetails);

  }

  @Override
  public void saveRoleDetails(RoleDetails roleDetails) {

    compositePKRoleDetailsRepository.save(roleDetails);

  }


}
