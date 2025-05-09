package bi.gov.obr.informationServices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PrevisionUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Exercice anneeBudgetaire;
    private String fileNamePrevision;
    private String fileNamePrevisionRevisE;

}
