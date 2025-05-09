package bi.gov.obr.informationServices.dto;


import bi.gov.obr.informationServices.enumeration.UtilitaireImport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtilitaireDto {
    UtilitaireImport typeFichierBase;
    private Long exerciceId;
    private boolean revision;
    private String previsionFileName;
}
