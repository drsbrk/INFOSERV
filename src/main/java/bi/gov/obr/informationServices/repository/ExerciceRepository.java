package bi.gov.obr.informationServices.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bi.gov.obr.informationServices.entity.Exercice;

@Repository
public interface ExerciceRepository extends JpaRepository<Exercice, Long> {

  List<Exercice> findAllByOrderByDateDebutDesc();

  List<Exercice> findAllByOrderByIdDesc();

  Page<Exercice> findAllByOrderByIdDesc(Pageable pageable);

  @Query("SELECT e FROM Exercice e WHERE e.dateDebut <= :currentDate AND e.dateFin >= :currentDate")
  Optional<Exercice> findByBetweenDateDebutAnAndDateFin(LocalDate currentDate);


  @Query(value = "SELECT ex FROM Exercice ex WHERE ex.dateDebut <= :currentDate AND ex.dateFin >= :currentDate")
  List<Exercice> findExerciceByBetweenDateDebutAnAndDateFin(@Param("currentDate") LocalDate currentDate);

  @Query(value = "SELECT id FROM exercice WHERE date_debut <= :currentDate AND date_fin >= :currentDate LIMIT 1", nativeQuery = true)
  Optional<Long> findExerciceIdByBetweenDateDebutAnAndDateFin(@Param("currentDate") LocalDate currentDate);

  @Query("SELECT e.id FROM Exercice e WHERE e.code = ?1")
  Long findIdByCode(String code);
}