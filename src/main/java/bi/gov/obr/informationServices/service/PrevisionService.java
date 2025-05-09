package bi.gov.obr.informationServices.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import bi.gov.obr.informationServices.entity.Prevision;
import bi.gov.obr.informationServices.entity.PrevisionUpload;

import java.util.List;

public interface PrevisionService {

    Prevision save(Prevision prevision);

    Prevision update(Long id, Prevision prevision);

    Prevision findById(Long id);

    List<Prevision> findAll();
    Page<Prevision> findAll(Pageable pageable);

    void deleteById(Long id);

    String findCodePrevisionById(Long idPrevision);

    boolean loadPrevisionFromExcel(MultipartFile file, Long exerciceId, PrevisionUpload previsionUpload, boolean isRevision);

    List<Prevision> loadPrevisionByAnneeBudgetaire(Long exerciceId);

    List<Prevision> loadPrevisionByAnneeBudgetaire(Long exerciceId, List<String> codeRecettes);
}