package bi.gov.obr.informationServices.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {
    if (authException instanceof DisabledException) {
      // response.sendError(HttpServletResponse.SC_FORBIDDEN, "Compte désactivé.
      // Contacter l'ADMIN de l'application");
      UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      boolean enabled = userDetails.isEnabled();
      if (enabled == false) {
        request.getSession().setAttribute("errorMessage",
            "Votre compte a été désactivé. Veuillez contacter l'administrateur de cette application!");
        response.sendRedirect(request.getContextPath() + "/login?error");

    }
  } else {
      request.getSession().setAttribute("errorMessage",
          "Veuillez d'abord vous identifier dans l'application");
      response.sendRedirect(request.getContextPath() + "/login?error");
    }

  }

}
