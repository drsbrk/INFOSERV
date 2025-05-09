package bi.gov.obr.informationServices.exceptionHandler;

import org.springframework.security.core.AuthenticationException;

public class BiUserDisabledException extends AuthenticationException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public BiUserDisabledException(String msg) {
    super(msg);
  }
}
