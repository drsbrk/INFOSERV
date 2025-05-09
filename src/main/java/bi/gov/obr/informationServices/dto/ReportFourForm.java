package bi.gov.obr.informationServices.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.utils.MonthAnneeBudgetaire;

@Getter
@Setter
public class ReportFourForm {

    private List<MonthAnneeBudgetaire> monthAnneeBudgetaires = new ArrayList<>();
    private List<Exercice> exercices = new ArrayList<>();
    private String monthAnneeBudgetaireSelected;
    private Long anneeBudgetaireSelected;
}
