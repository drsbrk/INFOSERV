package bi.gov.obr.informationServices.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivationDto {

  private int enabled;
  @NotBlank
  private long id;
}
