package bi.gov.obr.informationServices.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bi.gov.obr.informationServices.entity.SubMenu;

@Repository
public interface SousMenuRepository extends JpaRepository<SubMenu, Integer> {

  Optional<List<SubMenu>> findByDisplayName(String displayName);
}
