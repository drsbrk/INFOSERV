package bi.gov.obr.informationServices.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import bi.gov.obr.informationServices.utils.Helper;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecetteRapport {

    private String codeRecette;
    private String libelleRecette;
    private RecetteObr recetteObr;
    private BigDecimal previsionJan;
    private BigDecimal recetteJan;
    private BigDecimal previsionFev;
    private BigDecimal recetteFev;
    private BigDecimal previsionMar;
    private BigDecimal recetteMar;
    private BigDecimal previsionApr;
    private BigDecimal recetteApr;
    private BigDecimal previsionMay;
    private BigDecimal recetteMay;
    private BigDecimal previsionJun;
    private BigDecimal recetteJun;
    private BigDecimal previsionJul;
    private BigDecimal recetteJul;
    private BigDecimal previsionAug;
    private BigDecimal recetteAug;
    private BigDecimal previsionSep;
    private BigDecimal recetteSep;
    private BigDecimal previsionOct;
    private BigDecimal recetteOct;
    private BigDecimal previsionNov;
    private BigDecimal recetteNov;
    private BigDecimal previsionDec;
    private BigDecimal recetteDec;

    private String previsionJanS;
    private String recetteJanS;
    private String previsionFevS;
    private String recetteFevS;
    private String previsionMarS;
    private String recetteMarS;
    private String previsionAprS;
    private String recetteAprS;
    private String previsionMayS;
    private String recetteMayS;
    private String previsionJunS;
    private String recetteJunS;
    private String previsionJulS;
    private String recetteJulS;
    private String previsionAugS;
    private String recetteAugS;
    private String previsionSepS;
    private String recetteSepS;
    private String previsionOctS;
    private String recetteOctS;
    private String previsionNovS;
    private String recetteNovS;
    private String previsionDecS;
    private String recetteDecS;
    private BigDecimal totalS;

    public String getPrevisionJanS() {
        if (previsionJan != null)
            previsionJanS = Helper.formaterMontant().format(previsionJan);
        return previsionJanS;
    }

    public String getRecetteJanS() {
        if (recetteJan != null)
            return recetteJanS = Helper.formaterMontant().format(recetteJan);
        return "0";
    }

    public String getPrevisionFevS() {
        return previsionFevS;
    }

    public String getRecetteFevS() {
        if (recetteFev != null)
            return recetteFevS = Helper.formaterMontant().format(recetteFev);
        return "0";
    }

    public String getPrevisionMarS() {
        return previsionMarS;
    }

    public String getRecetteMarS() {
        if (recetteMar != null)
            return recetteMarS = Helper.formaterMontant().format(recetteMar);
        return "0";
    }

    public String getPrevisionAprS() {
        return previsionAprS;
    }

    public String getRecetteAprS() {
        if (recetteApr != null)
           return recetteAprS = Helper.formaterMontant().format(recetteApr);
        return "0";
    }

    public String getPrevisionMayS() {
        return previsionMayS;
    }

    public String getRecetteMayS() {
        if (recetteMay != null)
           return recetteMayS = Helper.formaterMontant().format(recetteMay);
        return "0";
    }

    public String getPrevisionJunS() {
        return previsionJunS;
    }

    public String getRecetteJunS() {
        if (recetteJun != null)
           return recetteJunS = Helper.formaterMontant().format(recetteJun);
        return "0";
    }

    public String getPrevisionJulS() {
        return previsionJulS;
    }

    public String getRecetteJulS() {
        if (recetteJul != null)
           return recetteJulS = Helper.formaterMontant().format(recetteJul);
        return "0";
    }

    public String getPrevisionAugS() {
        return previsionAugS;
    }

    public String getRecetteAugS() {
        if (recetteAug != null)
           return recetteAugS = Helper.formaterMontant().format(recetteAug);
        return "0";
    }

    public String getPrevisionSepS() {
        return previsionSepS;
    }

    public String getRecetteSepS() {
        if (recetteSep != null)
           return recetteSepS = Helper.formaterMontant().format(recetteSep);
        return "0";
    }

    public String getPrevisionOctS() {
        return previsionOctS;
    }

    public String getRecetteOctS() {
        if (recetteOct != null)
          return recetteOctS = Helper.formaterMontant().format(recetteOct);
        return "0";
    }

    public String getPrevisionNovS() {
        return previsionNovS;
    }

    public String getRecetteNovS() {
        if (recetteNov != null)
           return recetteNovS = Helper.formaterMontant().format(recetteNov);
        return "0";
    }

    public String getPrevisionDecS() {
        return previsionDecS;
    }

    public String getRecetteDecS() {
        if (recetteDec != null)
           return recetteDecS = Helper.formaterMontant().format(recetteDec);
        return "0";
    }

    @Override
    public String toString() {
        return "RecetteRapport{" +
                "codeRecette='" + codeRecette + '\'' +
                ", libelleRecette='" + libelleRecette + '\'' +
                ", recetteJan=" + recetteJan +
                ", recetteFev=" + recetteFev +
                ", recetteMar=" + recetteMar +
                ", recetteApr=" + recetteApr +
                ", recetteMay=" + recetteMay +
                ", recetteJun=" + recetteJun +
                ", recetteJul=" + recetteJul +
                ", recetteAug=" + recetteAug +
                ", recetteSep=" + recetteSep +
                ", recetteOct=" + recetteOct +
                ", recetteNov=" + recetteNov +
                ", recetteDec=" + recetteDec +
                '}';
    }
}
