package bi.gov.obr.informationServices.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bi.gov.obr.informationServices.dto.DashboardOneResponse;
import bi.gov.obr.informationServices.dto.DashboardTwoResponse;
import bi.gov.obr.informationServices.entity.PosteCollecte;

@Repository
public interface PosteCollecteRepository extends JpaRepository<PosteCollecte, Long> {

  List<PosteCollecte> findAllByOrderByCodeDesc();

  Page<PosteCollecte> findAllByOrderByCodeDesc(Pageable pageable);

  Optional<PosteCollecte> findByCode(String code);

  @Query(value = "SELECT * FROM postecollecte ORDER BY libelle ", nativeQuery = true)
  List<PosteCollecte> findPosteUsingSet();

  @Query(name = "PosteCollecte.getAllPosteCollecteWithRecetteObr", nativeQuery = true)
  List<Long> getAllPosteCollecteWithRecetteObr(LocalDate debutMois, LocalDate jMoinsUn, List<String> centreCollectes, List<String> codeRecettes, List<String> devises);

  @Query(name = "PosteCollecte.getAllPosteCollecteWithTypeRecetteObr", nativeQuery = true)
  List<Long> getAllPosteCollecteWithTypeRecetteObr(LocalDate debutMois, LocalDate jMoinsUn, List<String> centreCollectes, List<String> typeRecettes, List<String> devises);

  @Query(name = "PosteCollecte.getReportOneData", nativeQuery = true)
  List<DashboardOneResponse> getReportOneData(LocalDate debutMois, LocalDate jMoinsDeux,
      LocalDate jMoinsUn, List<String> centreDeCollecte, List<String> typesCodeRecettes, List<String> devises);

  @Query(name = "PosteCollecte.getSituationPeriodique", nativeQuery = true)
  List<DashboardTwoResponse> getSituationPeriodique(LocalDate startDate, LocalDate endDate,
      List<String> centreDeCollecte, List<String> typesRecettes, List<String> devises);
}
