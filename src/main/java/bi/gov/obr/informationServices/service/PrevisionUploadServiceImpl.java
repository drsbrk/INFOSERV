package bi.gov.obr.informationServices.service;

import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.entity.PrevisionUpload;
import bi.gov.obr.informationServices.repository.PrevisionRepository;
import bi.gov.obr.informationServices.repository.PrevisionUploadRepository;

@Service
public class PrevisionUploadServiceImpl implements PrevisionUploadService{


    private final PrevisionRepository previsionRepository;
    private final PrevisionUploadRepository previsionUploadRepository;
    private final ExerciceService exerciceService;

    public PrevisionUploadServiceImpl(PrevisionRepository previsionRepository, PrevisionUploadRepository previsionUploadRepository, ExerciceService exerciceService) {
        this.previsionRepository = previsionRepository;
        this.previsionUploadRepository = previsionUploadRepository;
        this.exerciceService = exerciceService;
    }

    @Override
    public PrevisionUpload findByAnneeBudgetaire(Long anneeBudgetaireId) {
        Exercice anneeBudgetaire = exerciceService.findById(anneeBudgetaireId);
        return previsionUploadRepository.findFirstByAnneeBudgetaire(anneeBudgetaire);
    }

    @Override
    public PrevisionUpload saveOrUpdate(PrevisionUpload previsionUpload) {
        return previsionUploadRepository.save(previsionUpload);
    }
}
