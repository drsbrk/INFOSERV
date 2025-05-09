package bi.gov.obr.informationServices.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.entity.Role;
import bi.gov.obr.informationServices.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {
  @Autowired
  private RoleRepository roleRepo;

  @Override
  public Role addRole(Role role) {
    return roleRepo.save(role);
  }

  @Override
  public List<Role> findAll() {
    return roleRepo.findAll();
  }


  @Override
  public Role findRole(Integer roleCode) {
    return roleRepo.findById(roleCode).get();
  }

  @Override
  public Role findRoleByName(String roleName) {
    Role role = roleRepo.findByName(roleName);
    return role;
  }

}
