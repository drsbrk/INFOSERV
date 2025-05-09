package bi.gov.obr.informationServices.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import bi.gov.obr.informationServices.entity.PosteCollecte;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PosteCollecteResponse {

    private PosteCollecte posteCollecte;
    private List<PosteCollecte> posteCollectes = new ArrayList<>();
}
