
package bi.gov.obr.informationServices.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UrlPathHelper;

import bi.gov.obr.informationServices.service.CustomUserDetailsService;
import bi.gov.obr.informationServices.utils.CustomAuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;

@Order(1)
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

  // private final UserDetailsService userDetailsService;

  @Bean
  public static String getCurrentDate() {
    String curDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
    return curDate;
  }

  @Autowired
  private CustomAuthenticationFailureHandler failureHandler;


  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests().antMatchers("/favicon.ico").permitAll()
    .antMatchers("/css/**", "/js/**", "/icons/fonts/**", "/icons/**", "/login").permitAll().anyRequest()
    .authenticated()

    .and().formLogin()
    .loginPage("/login").permitAll().usernameParameter("email")
    .failureHandler(failureHandler).defaultSuccessUrl("/index").and().logout()
    .logoutSuccessHandler(new LogoutSuccessHandler() {
      @Override
      public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) throws IOException, ServletException {
        UrlPathHelper helper = new UrlPathHelper();
        String context = helper.getContextPath(request);
        response.sendRedirect(context + "/login?logout");
      }
    }).logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout");
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/css/**", "/js/**", "/css/font/**");

  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
  }
}
