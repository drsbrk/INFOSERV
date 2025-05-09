package bi.gov.obr.informationServices.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import bi.gov.obr.informationServices.exceptionHandler.BiUserDisabledException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    if (exception.getCause() != null && exception.getCause().getClass().equals(BiUserDisabledException.class)) {
      request.getSession().setAttribute("errorMessage", exception.getMessage());
    } else {

      request.getSession().setAttribute("errorMessage", "Nom d'utilisateur ou mot de passe incorrect. Réessayer!");
    }
    // redirect to login page with error message
    response.sendRedirect(request.getContextPath() + "/login?error=true");
  }

}
