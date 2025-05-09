package bi.gov.obr.informationServices.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bi.gov.obr.informationServices.entity.RoleDetails;

@Repository
public interface CompositePKRoleDetailsRepository extends JpaRepository<RoleDetails, Integer> {
  @Modifying
  @Transactional
  @Query(value = "DELETE FROM detailsroles WHERE role_id=?1 AND menu_id=?2", nativeQuery = true)
  void deleteBycol1roleIdAndcol2menuId(int roleId, int menuId);

  @Query(" FROM RoleDetails WHERE role_id = ?1 AND menu_id = ?2")
  Optional<List<RoleDetails>> detailsByRole_idAndMenu_id(int roleId, int menuId);

  @Query("SELECT r.subMenuId FROM RoleDetails r WHERE r.role_id = ?1 AND r.menu_id = ?2")
  Optional<List<Integer>> getAssignedSubMenusPerRoleAndMenu(Integer role_id, int menu_id);
  @Query("SELECT DISTINCT r.menu_id FROM RoleDetails r WHERE r.role_id = ?1")
  Optional<List<Integer>> getProfileRoleDetails(int id);
}
