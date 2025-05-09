package bi.gov.obr.informationServices.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import bi.gov.obr.informationServices.entity.CompteImpot;

import java.util.List;

@Repository
public interface CompteImpotRepository extends JpaRepository<CompteImpot, Long> {

    @Query("SELECT c FROM CompteImpot c order by c.id desc ")
    List<CompteImpot> findAllOrderByIdDesc();
    @Query("SELECT c.id FROM CompteImpot c WHERE c.code = ?1")
    Long findIdByCode(String code);
    Long findIdByCodeAndTypeRecette(String code, String typeRecette);
    Page<CompteImpot> findAllByOrderById(Pageable pageable);
    @Query("SELECT c FROM CompteImpot c WHERE c.code IN ?1")
    List<CompteImpot> findAllByCodeIn(List<String> codes);
    @Query("select codeRecette.code FROM CompteImpot codeRecette where codeRecette.typeRecette IN ?1")
    List<String> findAllByCodeByType(List<String> typeRecettes);

    @Query("SELECT c FROM CompteImpot c WHERE c.codePrevision IN ?1")
    List<CompteImpot> findAllByCodePrevisionIn(List<String> codes);
}