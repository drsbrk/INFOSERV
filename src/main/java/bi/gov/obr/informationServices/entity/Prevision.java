package bi.gov.obr.informationServices.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import javax.persistence.*;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import bi.gov.obr.informationServices.utils.Helper;
import bi.gov.obr.informationServices.utils.MonthSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Prevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_id")
    private CompteImpot codeRecette;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercice_id")
    private Exercice anneeBudgetaire;
    @JsonSerialize(using = MonthSerializer.class)
    @Enumerated(EnumType.ORDINAL)
    private Month previsionMonth;
    private BigDecimal montantCti;
    private BigDecimal montantCda;
    private BigDecimal montantReviseCda;
    private BigDecimal montantReviseCti;
    private LocalDate dateEnregistrement;
    private LocalDate dateRevision;
    @Transient
    private String montantFormattE;
    @Transient
    private String montantRevisEFormattE;
    private int monthPriority;


    public String getDisplayedPrevisionMonth() {
        return StringUtils.capitalize(this.getPrevisionMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE));
    }

    public String getMontantFormattE() {
        if (montantCda != null) {
            montantFormattE = Helper.formaterMontant().format(montantCda);
        } else if (montantCti != null) {
            montantFormattE = Helper.formaterMontant().format(montantCti);
        } else {
            montantFormattE = "-";
        }
        return montantFormattE;
    }

    public String getMontantRevisEFormattE() {
        if (montantReviseCda != null) {
            montantRevisEFormattE = Helper.formaterMontant().format(montantReviseCda);
        } else if (montantReviseCti != null) {
            montantRevisEFormattE = Helper.formaterMontant().format(montantReviseCti);
        } else {
            montantRevisEFormattE = "-";
        }
        return montantRevisEFormattE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prevision prevision = (Prevision) o;
        return Objects.equals(codeRecette, prevision.codeRecette) && Objects.equals(anneeBudgetaire, prevision.anneeBudgetaire) && previsionMonth == prevision.previsionMonth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeRecette, anneeBudgetaire, previsionMonth);
    }

    public void calculateMonthPriority() {
        switch (previsionMonth) {
            case JULY:
                setMonthPriority(1);
                break;
            case AUGUST:
                setMonthPriority(2);
                break;
            case SEPTEMBER:
                setMonthPriority(3);
                break;
            case OCTOBER:
                setMonthPriority(4);
                break;
            case NOVEMBER:
                setMonthPriority(5);
                break;
            case DECEMBER:
                setMonthPriority(6);
                break;
            case JANUARY:
                setMonthPriority(7);
                break;
            case FEBRUARY:
                setMonthPriority(8);
                break;
            case MARCH:
                setMonthPriority(9);
                break;
            case APRIL:
                setMonthPriority(10);
                break;
            case MAY:
                setMonthPriority(11);
                break;
            case JUNE:
                setMonthPriority(12);
                break;
        }
    }

    @Override
    public String toString() {
        return "Prevision{" +
                "id=" + id +
                ", compte=" + codeRecette +
                ", exercice=" + anneeBudgetaire +
                ", previsionMonth=" + previsionMonth +
                ", montant=" + montantFormattE +
                ", montantRevisE=" + montantRevisEFormattE
                +
                ", dateEnregistrement=" + dateEnregistrement +
                ", dateRevision=" + dateRevision +
                ", montantFormattE='" + montantFormattE + '\'' +
                ", montantRevisEFormattE='" + montantRevisEFormattE + '\'' +
                '}';
    }
}