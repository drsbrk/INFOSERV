package bi.gov.obr.informationServices.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.entity.SubMenu;
import bi.gov.obr.informationServices.repository.SousMenuRepository;

@Service
public class SubMenuServiceImpl implements SubMenuService {
  @Autowired
  private SousMenuRepository subRepo;
  @Override
  public List<SubMenu> findAll() {
    return subRepo.findAll();
  }

  @Override
  public SubMenu findSubMenuByID(int id) {
    return subRepo.findById(id).get();
  }

  @Override
  public SubMenu save(@Valid SubMenu subMenu) {
    return subRepo.save(subMenu);
  }
}
