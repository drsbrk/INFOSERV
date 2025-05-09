package bi.gov.obr.informationServices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import bi.gov.obr.informationServices.enums.PrevisionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrevisionDto {

    private Long id;
    private LocalDate uploadDate;
    private String fileName;
    private PrevisionType PREVISION_TYPE;
    private Long exercice;
    private LocalDate debutPrevision;
    private LocalDate finPrevision;
}
