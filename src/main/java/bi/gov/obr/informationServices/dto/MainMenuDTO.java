package bi.gov.obr.informationServices.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainMenuDTO {

  private Integer id;
  @NotEmpty(message = "Nom obligatoire")
  private String name;
  @NotEmpty(message = "Icône obligatoire")
  private String icon;
  private String uri;

}
