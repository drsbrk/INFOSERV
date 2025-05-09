package bi.gov.obr.informationServices.enumeration;

import lombok.Getter;

@Getter
public enum UtilitaireImport {

    SKIP_0(0, "SKIP_TO_1"),
    CODE_RECETTE(1, "Code Recette"),
    PREVISION(2, "Prévision"),
    RECETTEVIEW(3, "Recette de l'OBR");

    private final int indexUtilitaire;
    private final String libelleUtilitaire;

    UtilitaireImport(int indexUtilitaire, String libelleUtilitaire) {
        this.indexUtilitaire = indexUtilitaire;
        this.libelleUtilitaire = libelleUtilitaire;
    }

}
