package bi.gov.obr.informationServices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.entity.Exercice;
import bi.gov.obr.informationServices.entity.PosteCollecte;
import bi.gov.obr.informationServices.utils.MonthAnneeBudgetaire;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecetteObrForm {

    private Long selectedAnneeBudgetaireId;
    private List<Long> selectedCodeRecetteIds;
    private Long selectedCodeRecetteId;
    private List<Exercice> anneeBudgetaires = new ArrayList<>();
    private List<CompteImpot> codeRecettes = new ArrayList<>();
    private List<MonthAnneeBudgetaire> monthAnneeBudgetaires = new ArrayList<>();
    List<MonthAnneeBudgetaire> selectedMonthAnneeBudgetaires = new ArrayList<>();
    private List<PosteCollecte> posteCollectes = new ArrayList<>();
    private long selectedPosteCollecteId;
    Long selectedMonthAnneeBudgetaireId;
    private int draw;
    private int start;
    private int length;
    private long recordsTotal;
    private long recordsFiltered;
    private boolean inclurePrevision;
    private LocalDate choosenDate = LocalDate.now();
}
