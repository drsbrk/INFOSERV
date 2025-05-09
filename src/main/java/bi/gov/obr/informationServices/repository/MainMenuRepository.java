package bi.gov.obr.informationServices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bi.gov.obr.informationServices.entity.MainMenu;

public interface MainMenuRepository extends JpaRepository<MainMenu, Integer> {


  Optional<List<MainMenu>> findByNameContaining(String name);

  MainMenu save(Optional<MainMenu> menu);

}
