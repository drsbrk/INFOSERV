package bi.gov.obr.informationServices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.entity.Prevision;
import bi.gov.obr.informationServices.entity.PrevisionUpload;
import bi.gov.obr.informationServices.repository.ExerciceRepository;
import bi.gov.obr.informationServices.repository.PrevisionRepository;
import bi.gov.obr.informationServices.utils.Helper;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrevisionServiceImpl implements PrevisionService {


    private final PrevisionRepository previsionRepository;
    private final ExerciceService exerciceService;
    private final CompteImpotService compteImpotService;
    private final PrevisionUploadService previsionUploadService;
    private final ExerciceRepository exerciceRepository;
    private final AppSettingService appSettingService;

    @Override
    public Prevision save(Prevision prevision) {
        if (prevision == null)
            throw new IllegalArgumentException("Veuillez donner une prévision valide");

        Long samePrevisionId = previsionRepository.
                findIdPrevisionByCOdeRecetteAndExerciceAndMonth(prevision.getCodeRecette(), prevision.getAnneeBudgetaire(), prevision.getPrevisionMonth()).
                orElse(null);

        if (samePrevisionId != null)
            throw new IllegalArgumentException("Une prévision de même type existe déjà pour cette période");


        prevision.setDateEnregistrement(LocalDate.now());
        return previsionRepository.save(prevision);
    }

    @Override
    @Transactional
    public Prevision update(Long id, Prevision prevision) {

        if (id.longValue() != prevision.getId().longValue())
            throw new IllegalArgumentException("L\'id en paramètre ne correspond pas à celui de la prévision");

//        prevision.setAnneeBudgetaire(anneBudgetaire);
        prevision.setDateRevision(LocalDate.now());
//        prevision.setCodeRecette(codeRecette);
        Prevision previsionFromDB = previsionRepository.findByIdWithExerciceAndCodeRecette(id).orElseThrow(() -> new IllegalArgumentException("Prévision non trouvée"));
        previsionFromDB = prevision;
        return previsionRepository.save(previsionFromDB);
    }

    @Override
    @Transactional
    public Prevision findById(Long id) {
        Optional<Prevision> prevision = previsionRepository.findByIdWithExerciceAndCodeRecette(id);
        System.out.println("prevision Exercice " + prevision.get().getAnneeBudgetaire());
        return prevision.orElse(null);
    }

    @Override
    public List<Prevision> findAll() {
        return previsionRepository.findAll();
    }

    @Override
    @Transactional
    public Page<Prevision> findAll(Pageable pageable) {

        Page<Prevision> previsionPage = previsionRepository.findAllByOrderByIdDesc(pageable);
        previsionPage.stream().forEach(System.out::println);
        return previsionPage;
    }

    @Override
    public void deleteById(Long id) {
        previsionRepository.deleteById(id);
    }

    @Override
    public String findCodePrevisionById(Long idPrevision) {
        return null;
    }

    @Override
    public boolean loadPrevisionFromExcel(MultipartFile file, Long exerciceId, PrevisionUpload previsionUpload, boolean isRevision) {

        boolean operationStatus = false;
        Set<CompteImpot> compteToCreate = new HashSet<>();

        try {
            Set<Prevision> previsions = Helper.readPrevisionFromExcel(file.getInputStream(), appSettingService.getAppSetting());
            List<Prevision> previsionsToRevise;
            if (isRevision || previsionUpload != null) {
                Exercice anneeBudgetaire = exerciceRepository.findById(exerciceId).orElseThrow(
                        () -> new IllegalArgumentException(" L'année budgétaire n'existe pas. ")
                );
                previsionsToRevise = previsionRepository.findByAnneeBudgetaire(anneeBudgetaire);
            } else {
                previsionsToRevise = new ArrayList<>();
            }
            if (previsions.isEmpty())
                throw new IllegalArgumentException("Aucune prévision trouvée dans le fichier excel");

            Exercice anneeBudgetaire = exerciceService.findById(exerciceId);
            // Load All compte comptable (code recette)
            List<CompteImpot> compteImpots = compteImpotService.findAllByCodeIn(previsions.stream()
                    .map(prev -> prev.getCodeRecette().getCode())
                    .collect(Collectors.toList()));

            // Affect each prevision with code recette
            previsions.forEach(prev -> {
                CompteImpot compteImpot = compteImpots.stream()
                        .filter(compte -> compte.getCode().equals(prev.getCodeRecette().getCode()))
                        .findFirst().orElse(null);
                if(prev.getCodeRecette().getCode().equals("715240"))
                {
                    System.out.println("=======================================");
                    System.out.println("=======================================");
                    System.out.println("=======================================");
                    System.out.println("=======================================");
                    System.out.println("Compte Impot " + prev.getCodeRecette().getCode());
                    System.out.println("=======================================");
                    System.out.println("=======================================");
                    System.out.println("=======================================");
                }
                if (compteImpot == null) {
                    CompteImpot compte = new CompteImpot(prev.getCodeRecette().getCode(),prev.getCodeRecette().getLibelle(), prev.getCodeRecette().getTypeRecette());
                    compteToCreate.add(compte);
                    return;
                }
                // If it's revision then take the existing prevision and update the montantRevisE field
                if (isRevision || !previsionsToRevise.isEmpty()) {
                    Prevision prevFound = previsionsToRevise.stream().filter(prevToRevise -> prevToRevise.getCodeRecette().getCode()
                            .equals(prev.getCodeRecette().getCode()) && (prevToRevise.getPrevisionMonth().getValue() == prev.getPrevisionMonth().getValue())).findFirst().orElse(null);

                    if (prevFound != null && isRevision && prev.getMontantReviseCda() != null)
                        prevFound.setMontantReviseCda(prev.getMontantReviseCda());
                    else if (prevFound != null && isRevision && prev.getMontantReviseCti() != null)
                        prevFound.setMontantReviseCti(prev.getMontantReviseCti());
                }
                //When it's a new prevision
                else {

                    prev.setCodeRecette(compteImpot);
                    prev.setDateEnregistrement(LocalDate.now());
                    prev.setAnneeBudgetaire(anneeBudgetaire);
                }

            });

            if (!compteToCreate.isEmpty()) {
//                compteToCreate.forEach(compte -> {
//                    compteImpotService.saveCompteImpot(compte, null);
//                });
                throw new IllegalArgumentException("Veuillez créer les codes recettes suivants car ils n'existent pas dans la liste des codes recettes. " + compteToCreate.stream().map(compt -> "Code comptable " + compt.getCode() + " type Recette " + compt.getTypeRecette()).collect(Collectors.joining(", ")));
            }

            if (!isRevision && (previsionsToRevise == null || previsionsToRevise.isEmpty()))
                previsionRepository.saveAll(previsions);
            else
                previsionRepository.saveAll(previsionsToRevise);
            previsionUploadService.saveOrUpdate(previsionUpload);
            operationStatus = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return operationStatus;
    }

    @Override
    @Transactional
    public List<Prevision> loadPrevisionByAnneeBudgetaire(Long exerciceId) {
        Exercice anneeBudgetaire = exerciceService.findById(exerciceId);
        if (anneeBudgetaire == null)
            throw new IllegalArgumentException("L'année budgetaire choisi n'existe pas.");
        List<Prevision> previsions = previsionRepository.findByAnneeBudgetaire(anneeBudgetaire);
//        previsions.forEach(prev -> prev.getAnneeBudgetaire().getId());
        return previsions;
    }

    @Override
    @Transactional
    public List<Prevision> loadPrevisionByAnneeBudgetaire(Long exerciceId, List<String> codeRecettes) {

        Exercice anneeBudgetaire = exerciceService.findById(exerciceId);
        if (anneeBudgetaire == null)
            throw new IllegalArgumentException("L'année budgetaire choisi n'existe pas.");
        List<Prevision> previsions = previsionRepository.findByAnneeBudgetaireAndCodeRecetteIn(anneeBudgetaire, codeRecettes);


        if (codeRecettes != null && !codeRecettes.isEmpty() && previsions != null) {
            final BigDecimal[] total = {BigDecimal.ZERO};
            previsions.forEach(pr -> {
                if (pr.getMontantCda() != null)
                    total[0] = total[0].add(pr.getMontantCda());
                else if (pr.getMontantCti() != null)
                    total[0] = total[0].add(pr.getMontantCti());
            });

            System.out.println(" ===>>> " + codeRecettes.get(0) + " total " + total[0]);
        }
        return previsions;
    }
}