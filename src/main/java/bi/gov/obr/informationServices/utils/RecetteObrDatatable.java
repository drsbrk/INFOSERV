package bi.gov.obr.informationServices.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import bi.gov.obr.informationServices.entity.RecetteRapport;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecetteObrDatatable {

    private int draw;
    private int start;
    private int length;
    private long recordsTotal;
    private long recordsFiltered;
    private int totalPages;
    private long totalItems;
    private int currentPage;
    private int pageSize;
    private List<RecetteRapport> recettesData = new ArrayList<>();
    private RecetteRapport recetteRapportTotal;
}
