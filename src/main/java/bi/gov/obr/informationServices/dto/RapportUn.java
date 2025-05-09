package bi.gov.obr.informationServices.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import bi.gov.obr.informationServices.entity.PosteCollecte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RapportUn {
  private Set<PosteCollecte> posteCollectes = new HashSet<>();
  private LocalDate localDate;
}
