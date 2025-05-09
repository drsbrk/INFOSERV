package bi.gov.obr.informationServices.repository;

import java.time.Month;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.entity.Prevision;

public interface PrevisionRepository extends JpaRepository<Prevision, Long> {

    @Query("SELECT prev FROM Prevision prev join prev.codeRecette ORDER BY prev.id DESC")
    Page<Prevision> findAllByOrderByIdDesc(Pageable pageable);

    @Query("SELECT prev FROM Prevision prev join fetch prev.codeRecette join fetch prev.anneeBudgetaire WHERE prev.anneeBudgetaire = ?1 ORDER BY prev.id DESC")
    List<Prevision> findByAnneeBudgetaire(Exercice exerciceId);

    @Query("SELECT prev FROM Prevision prev join fetch prev.codeRecette join fetch prev.anneeBudgetaire WHERE prev.anneeBudgetaire = ?1 and prev.codeRecette.code in ?2 ORDER BY prev.monthPriority ASC")
    List<Prevision> findByAnneeBudgetaireAndCodeRecetteIn(Exercice exerciceId, List<String> codeRecettes);

    @Query("SELECT prev FROM Prevision prev join fetch prev.codeRecette WHERE prev.id = ?1")
    Optional<Prevision> findByIdWithExerciceAndCodeRecette(Long id);

    @Query("SELECT prev.id FROM Prevision prev WHERE prev.codeRecette = ?1 AND prev.anneeBudgetaire = ?2 AND prev.previsionMonth = ?3")
    Optional<Long> findIdPrevisionByCOdeRecetteAndExerciceAndMonth(CompteImpot codeRecetee, Exercice annneBudgetaire, Month previsionMonth);

    @Query("select max(prev.id) from Prevision prev where prev.anneeBudgetaire = ?1")
    Long findOneByAnneeBudgetaire(Exercice exercice);

}
