package bi.gov.obr.informationServices.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.entity.Prevision;

@Getter
@Setter
@NoArgsConstructor
public class PrevisionRecap {

    private String id;
    private Exercice annEeBudgetaire;
    private String codeRecette;
    private String libelleRecette;
    private List<Prevision> previsionDetails = new ArrayList<>();

    @Override
    public String toString() {
        return "PrevisionRecap{" +
                "annEeBudgetaire=" + annEeBudgetaire +
                ", codeRecette='" + codeRecette + '\'' +
                ", LibelleRecette='" + libelleRecette + '\'' +
                ", previsionDetails=" + previsionDetails +
                '}';
    }
}
