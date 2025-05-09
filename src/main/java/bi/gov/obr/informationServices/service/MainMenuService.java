package bi.gov.obr.informationServices.service;

import java.util.List;
import java.util.Optional;

import bi.gov.obr.informationServices.entity.MainMenu;

public interface MainMenuService {
  MainMenu addNewMainMenu(MainMenu mainMenu);

  List<MainMenu> findAll();

  MainMenu findMainMenuById(int id);

  Optional<MainMenu> findMainMenuById(String id);
}
