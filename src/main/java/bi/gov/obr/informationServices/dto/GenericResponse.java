package bi.gov.obr.informationServices.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponse {

    private String message;
    private int statusCode;

}
