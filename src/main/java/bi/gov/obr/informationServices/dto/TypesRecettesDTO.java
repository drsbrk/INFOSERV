package bi.gov.obr.informationServices.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypesRecettesDTO {
  private String[] typesRecettes = new String[] { "01", "02", "03" };
}
