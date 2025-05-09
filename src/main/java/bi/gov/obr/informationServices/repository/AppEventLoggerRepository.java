package bi.gov.obr.informationServices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bi.gov.obr.informationServices.entity.AppEventLogger;

@Repository
public interface AppEventLoggerRepository extends JpaRepository<AppEventLogger, Long> {
}
