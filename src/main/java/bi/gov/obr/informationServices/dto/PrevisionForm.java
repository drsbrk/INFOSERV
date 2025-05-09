package bi.gov.obr.informationServices.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.entity.Prevision;

@NoArgsConstructor
@Getter
@Setter
public class PrevisionForm {

    private Long id;
    private Prevision prevision;
    private List<Exercice> anneeBudgetaires = new ArrayList<>();
    private List<CompteImpot> codeRecettes = new ArrayList<>();
    private Long anneeBudgetaireTableId;
    private Long selectedCodeRecetteId;
    private Long selectedAnneeBudgetId;
    private List<String> months = new ArrayList<>();
    private String selectedMonth;
    private BigDecimal montantCti;
    private BigDecimal montantCda;
    private BigDecimal montantRevisECti;
    private BigDecimal montantRevisECda;

}



