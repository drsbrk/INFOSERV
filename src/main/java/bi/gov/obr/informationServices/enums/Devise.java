package bi.gov.obr.informationServices.enums;

public enum Devise {

  SKIP_0("BEGIN_TO_1", "00"),
  BIF("BIF", "01"), USD("US $", "02"), EURO("EUR", "03");

  public static Devise getDevise(String code) {
    switch (code){
    case "01":
      return BIF;
    case "02":
      return USD;
    case "03":
      return EURO;
    default:
      return BIF;
    }
  }
  private final String libelle;
  private final String code;

  Devise(String libelle, String code) {
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
