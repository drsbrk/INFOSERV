package bi.gov.obr.informationServices.exceptionHandler;

import org.springframework.dao.DataIntegrityViolationException;

public class DbException extends DataIntegrityViolationException {


    public DbException(String msg) {
        super(msg);
    }

    public DbException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
