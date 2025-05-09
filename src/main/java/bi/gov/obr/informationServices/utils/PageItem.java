package bi.gov.obr.informationServices.utils;

import bi.gov.obr.informationServices.enums.PageItemType;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PageItem {

    private PageItemType pageItemType;

    private int index;

    private boolean active;

}
