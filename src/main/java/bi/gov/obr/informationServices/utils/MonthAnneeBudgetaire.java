package bi.gov.obr.informationServices.utils;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class MonthAnneeBudgetaire {

    private String monthId;
    private String mois;
    private int anneE;

    private String moisAnneeBudgetaire;

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public int getAnneE() {

        return anneE;
    }

    public void setAnneE(int anneE) {
        this.anneE = anneE;
    }

    public String getMoisAnneeBudgetaire() {
        moisAnneeBudgetaire = mois + " " + (anneE == 0 ? "" : anneE);
        return moisAnneeBudgetaire;
    }

    public void setMoisAnneeBudgetaire(String moisAnneeBudgetaire) {
        this.moisAnneeBudgetaire = moisAnneeBudgetaire;
    }
}
