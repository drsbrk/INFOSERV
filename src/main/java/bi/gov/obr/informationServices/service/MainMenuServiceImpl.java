package bi.gov.obr.informationServices.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.entity.MainMenu;
import bi.gov.obr.informationServices.repository.MainMenuRepository;

@Service
public class MainMenuServiceImpl implements MainMenuService {
  @Autowired
  private MainMenuRepository mainRepo;
  @Override
  public MainMenu addNewMainMenu(MainMenu mainMenu) {
    return mainRepo.save(mainMenu);
  }

  @Override
  public List<MainMenu> findAll() {
    Sort sortBy = Sort.by(Sort.Direction.ASC, "name");
    List<MainMenu> mainMenuList = mainRepo.findAll(sortBy);
    return mainMenuList;

  }

  @Override
  public MainMenu findMainMenuById(int id) {
    if (id != 0) {
      Optional<MainMenu> menu = Optional.ofNullable(mainRepo.findById(id).orElse(null));
      if (menu.isPresent()) {
        return menu.get();
      } else {
        return null;
      }
    }
    return null;
  }

  @Override
  public Optional<MainMenu> findMainMenuById(String id) {
    int param = Integer.parseInt(id);
    return Optional.of(mainRepo.findById(param).orElseGet(null));
  }


}
