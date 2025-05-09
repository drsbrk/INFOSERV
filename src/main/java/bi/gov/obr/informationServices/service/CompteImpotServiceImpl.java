package bi.gov.obr.informationServices.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bi.gov.obr.informationServices.entity.AppEventLogger;
import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.repository.AppEventLoggerRepository;
import bi.gov.obr.informationServices.repository.CompteImpotRepository;
import bi.gov.obr.informationServices.utils.Helper;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompteImpotServiceImpl implements CompteImpotService {

    private final CompteImpotRepository compteImpotRepository;
    private final AppEventLoggerRepository appEventLoggerRepository;

    public CompteImpotServiceImpl(CompteImpotRepository compteImpotRepository, AppEventLoggerRepository appEventLoggerRepository) {

        this.compteImpotRepository = compteImpotRepository;
        this.appEventLoggerRepository = appEventLoggerRepository;
    }

    @Override
    public CompteImpot saveCompteImpot(CompteImpot compteImpot, AppEventLogger appEventLogger) {

        Long existingCode = compteImpotRepository.findIdByCodeAndTypeRecette(compteImpot.getCode(), compteImpot.getTypeRecette());
        if (existingCode != null) {
            throw new EntityExistsException("Le code du compte impot existe déjà");
        }

        compteImpot = compteImpotRepository.save(compteImpot);

        if (compteImpot.getId() != null && appEventLogger != null) {
            String appEventLoggerJson = Helper.getJsonString(compteImpot);
            System.out.println("AppEventLogger in JSON format : " + appEventLoggerJson);
            appEventLogger.setEntityJson(appEventLoggerJson);
            appEventLoggerRepository.save(appEventLogger);
        }
        return compteImpot;
    }

    @Override
    public List<CompteImpot> findAllCompteImpot() {
        List<CompteImpot> compteImpots = compteImpotRepository.findAllOrderByIdDesc();
        return compteImpots;
    }

    @Override
    public CompteImpot findById(Long idCompteImpot) {
        return compteImpotRepository.findById(idCompteImpot).orElseThrow(
                () -> new EntityNotFoundException("Compte impot non trouvé"));
    }

    @Override
    @Modifying(clearAutomatically = true)
    public CompteImpot updateCompteImpot(Long codeCompteImpot, CompteImpot compteImpot, AppEventLogger appEventLogger) {

        if (codeCompteImpot == null || compteImpot.getId() == null) {
            throw new IllegalArgumentException("L'identifiant du compte impot est null");
        }
        if (codeCompteImpot != compteImpot.getId().longValue()) {
            throw new IllegalArgumentException("L'identifiant du compte impot ne correspond pas à l'identifiant de l'objet compte impot");
        }

        Long existingCode = compteImpotRepository.findIdByCode(compteImpot.getCode());
        if (existingCode != null && existingCode.longValue() != compteImpot.getId()) {
            throw new EntityExistsException("Le code du compte impot existe déjà");
        }


        CompteImpot compteImpotToUpdate = compteImpotRepository.findById(codeCompteImpot).orElseThrow(
                () -> new EntityNotFoundException("Compte impôt inéxistant")
        );
        compteImpotToUpdate = compteImpot;
        compteImpotToUpdate = compteImpotRepository.save(compteImpotToUpdate);
        if (compteImpotToUpdate.getId() != null) {
            String appEventLoggerJson = Helper.getJsonString(compteImpotToUpdate);
            appEventLogger.setEntityJson(appEventLoggerJson);
            appEventLoggerRepository.save(appEventLogger);
        }
        return compteImpotToUpdate;
    }

    @Override
    public boolean deleteCompteImpot(Long idCompteImpot) {
        compteImpotRepository.deleteById(idCompteImpot);
        return true;
    }

    @Override
    public Page<CompteImpot> findAllCompteImpot(Pageable pageable) {
        return compteImpotRepository.findAllByOrderById(pageable);
    }

    @Override
    public boolean loadCompteImpotFromExcel(MultipartFile file) {


        //read excel file
        boolean operationStatus = false;
        try {
            log.info("Loading Compte Impot from Excel file");
            List<CompteImpot> compteImpots = Helper.readCompteImpotFromExcel(file.getInputStream());

            if(compteImpots.isEmpty()){
                throw new IllegalArgumentException("Le fichier fourni est vide");
            }

            List<CompteImpot> existingCompteImpots = compteImpotRepository.findAllByCodeIn(compteImpots.stream()
                    .map(CompteImpot::getCode)
                    .collect(Collectors.toList()));
            if (!existingCompteImpots.isEmpty()) {
                compteImpots.stream().forEach(
                        compteImpot -> {
                            CompteImpot existingCompteImpot = existingCompteImpots.stream()
                                    .filter(compteImpot1 -> compteImpot1.getCode().equals(compteImpot.getCode()))
                                    .findFirst().orElse(null);
                            if (existingCompteImpot != null) {
                                compteImpot.setId(existingCompteImpot.getId());
                            }
                        }
                );
            }

            compteImpotRepository.saveAll(compteImpots);
            operationStatus = true;

        } catch (Exception e) {
            log.error("Error loading Compte Impot from Excel file : " + e.getMessage());
            throw new IllegalArgumentException("L'import n'est pas passé correctement. Veuillez contacter l'administrateur pour plus de précision");
        }

        return operationStatus;
    }

    @Override
    public List<CompteImpot> findAllByCodeIn(List<String> codes) {
        return compteImpotRepository.findAllByCodeIn(codes);
    }

    @Override
    public List<CompteImpot> findAllByCodePrevisionIn(List<String> codes) {
        return compteImpotRepository.findAllByCodePrevisionIn(codes);
    }
}