package bi.gov.obr.informationServices.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import bi.gov.obr.informationServices.enums.UserAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "app_event_logger")
public class AppEventLogger {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String username;
    private String ipAdress;
    private UserAction USER_ACTION;
    private LocalDateTime dateAction;
    private String entityJson;
}
