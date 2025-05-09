package bi.gov.obr.informationServices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import bi.gov.obr.informationServices.entity.CompteImpot;
import bi.gov.obr.informationServices.enums.TypeCodeRecette;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CodeRecetteForm {

    private CompteImpot compteImpot;
    private TypeCodeRecette typeRecette;

    public static List<TypeCodeRecette> typeRecettes;

    static {
        typeRecettes =  Arrays.stream(TypeCodeRecette.values()).skip(1).collect(Collectors.toList());
    }

}
