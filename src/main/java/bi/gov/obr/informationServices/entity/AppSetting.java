package bi.gov.obr.informationServices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class AppSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int previsionFirstMonthColumn;
    private int previsionLastMonthColumn;
    private LocalDate lastlyModifiedAt;
    private boolean actual;
    @OneToOne
    private User  modifiedBy;

    @Override
    public String toString() {
        return "AppSetting{" +
                "previsionFirstMonthColumn=" + previsionFirstMonthColumn +
                ", previsionLastMonthColumn=" + previsionLastMonthColumn +
                ", actual=" + actual +
                '}';
    }
}
