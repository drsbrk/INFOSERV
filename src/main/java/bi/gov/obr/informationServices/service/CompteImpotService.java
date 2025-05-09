package bi.gov.obr.informationServices.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import bi.gov.obr.informationServices.entity.AppEventLogger;
import bi.gov.obr.informationServices.entity.CompteImpot;

import java.util.List;

public interface CompteImpotService {

    CompteImpot saveCompteImpot(CompteImpot compteImpot, AppEventLogger appEventLogger);

    List<CompteImpot> findAllCompteImpot();
    CompteImpot findById(Long idCompteImpot);

    CompteImpot updateCompteImpot(Long idCompteImpot, CompteImpot compteImpot, AppEventLogger appEventLogger);

    boolean deleteCompteImpot(Long idCompteImpot);

    Page<CompteImpot> findAllCompteImpot(Pageable pageable);

    boolean loadCompteImpotFromExcel(MultipartFile file);

    List<CompteImpot> findAllByCodeIn(List<String> codes);
    List<CompteImpot> findAllByCodePrevisionIn(List<String> codes);
}