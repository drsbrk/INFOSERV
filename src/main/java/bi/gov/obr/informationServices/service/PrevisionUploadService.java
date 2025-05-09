package bi.gov.obr.informationServices.service;

import bi.gov.obr.informationServices.entity.PrevisionUpload;

public interface PrevisionUploadService {

    PrevisionUpload findByAnneeBudgetaire(Long anneeBudgetaireId);
    PrevisionUpload saveOrUpdate(PrevisionUpload previsionUpload);

}
