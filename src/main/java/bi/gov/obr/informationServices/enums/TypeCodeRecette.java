package bi.gov.obr.informationServices.enums;

public enum TypeCodeRecette {


  SKIP_0("BEGIN_TO_1", "00"), RECETTE_FISCALE("Recettes RCMS", "01"),
  RECETTE_DOUANIERE("Recettes ASYCUDA", "02"), RECETTE_NON_FISCALE("Recettes SIGIBU", "03");

  public static TypeCodeRecette getTypeRecetteByCode(String code){
    switch (code){
    case "01":
      return RECETTE_FISCALE;
    case "02":
      return RECETTE_DOUANIERE;
    case "03":
      return RECETTE_NON_FISCALE;
    default:
      return RECETTE_FISCALE;
    }
  }
  private final String libelle;
  private final String code;

  private TypeCodeRecette(String libelle, String code) {
    this.libelle = libelle;
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public String getLibelle() {
    return libelle;
  }
}
