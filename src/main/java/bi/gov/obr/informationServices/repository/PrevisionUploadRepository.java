package bi.gov.obr.informationServices.repository;

import org.springframework.data.repository.CrudRepository;

import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.entity.PrevisionUpload;

public interface PrevisionUploadRepository extends CrudRepository<PrevisionUpload, Long> {

    PrevisionUpload findFirstByAnneeBudgetaire(Exercice anneeBudgetaire);

}
