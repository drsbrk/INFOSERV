package bi.gov.obr.informationServices.dto;

import java.math.BigDecimal;

import bi.gov.obr.informationServices.utils.Helper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportOneProjection implements Comparable<ReportOneProjection> {

    private String code;
    private String libelle;
    private String currency;
    private Long montantRecetteJMoinsDeux;
    private Long montantRecetteJMoinsUn;
    private Long variation;
    private String montantRecetteJMoinsUnS;
    private String montantRecetteJMoinsDeuxS;
    private String variationS;
    /**
     * These field concerns the second report. The amount collected within a given period of time
     */
    private Long montantRecetteCollecte;
    /**
     * These field helps to display the amount of the field 'montantRecetteCollecte" with a thousand separator
     */
    private String montantRecetteCollecteS;

    public ReportOneProjection(String code, String libelle,
                               BigDecimal montantRecetteJMoinsDeux, BigDecimal montantRecetteJMoinsUn) {
        this.code = code;
        this.libelle = libelle;
        this.montantRecetteJMoinsUn = montantRecetteJMoinsUn.longValue();
        this.montantRecetteJMoinsDeux = montantRecetteJMoinsDeux.longValue();
    }

    public ReportOneProjection(String code, String libelle, String currency,
                               BigDecimal montantRecetteJMoinsDeux, BigDecimal montantRecetteJMoinsUn) {
        this.code = code;
        this.libelle = libelle;
        this.currency = currency;
        this.montantRecetteJMoinsUn = montantRecetteJMoinsUn.longValue();
        this.montantRecetteJMoinsDeux = montantRecetteJMoinsDeux.longValue();
    }

    public ReportOneProjection(String code, String libelle, String currency, BigDecimal montantRecetteCollecte) {
        this.code = code;
        this.libelle = libelle;
        this.currency = currency;
        this.montantRecetteCollecte = montantRecetteCollecte.longValue();
    }

    @Override
    public int compareTo(ReportOneProjection o) {

        if (this.getCurrency().compareTo(o.getCurrency()) != 0) {
            return o.getCurrency().compareTo(this.getCurrency());
        } else {
            return this.getCode().compareTo(o.getCode());
        }

    }


    public String getMontantRecetteJMoinsDeuxS() {
        if (montantRecetteJMoinsDeux != null) {
            return Helper.formaterMontant().format(montantRecetteJMoinsDeux);
        }
        return "0";
    }


    public String getMontantRecetteJMoinsUnS() {
        if (montantRecetteJMoinsUn != null) {
            return Helper.formaterMontant().format(montantRecetteJMoinsUn);
        }
        return "0";
    }

    public Long getVariation() {
        if (montantRecetteJMoinsUn == null) {
          montantRecetteJMoinsUn = 0L;
        }
        if (montantRecetteJMoinsDeux == null) {
          montantRecetteJMoinsDeux = 0L;
        }

        return montantRecetteJMoinsUn - montantRecetteJMoinsDeux;
    }

    public String getVariationS() {
        if (montantRecetteJMoinsUn == null) {
            montantRecetteJMoinsUn = 0L;
        }
        if (montantRecetteJMoinsDeux == null) {
            montantRecetteJMoinsDeux = 0L;
        }

        return Helper.formaterMontant().format(montantRecetteJMoinsUn - montantRecetteJMoinsDeux);
    }

    public String getMontantRecetteCollecteS() {
        if (montantRecetteCollecte == null) {
            montantRecetteCollecte = 0L;
        }

        return Helper.formaterMontant().format(montantRecetteCollecte);

    }


}
