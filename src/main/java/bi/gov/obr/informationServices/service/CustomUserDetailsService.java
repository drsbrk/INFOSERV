package bi.gov.obr.informationServices.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import bi.gov.obr.informationServices.entity.User;
import bi.gov.obr.informationServices.exceptionHandler.BiUserDisabledException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    User user = userService.findUserByUsername(email);

    if (user != null) {
      if (user.getEnabled() == 1 || user.getEnabled() == -1) {
        /*
         * return new
         * org.springframework.security.core.userdetails.User(user.getEmail(),
         * user.getPassword(),
         *
         * user.getRoles().stream().map((role) -> new
         * SimpleGrantedAuthority(role.getName())) .collect(Collectors.toList()));
         */
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName())));
      } else {
        System.out.println("Got this sos far");
        throw new BiUserDisabledException("Compte désactivé. Veuillez contacter l'administrateur !");
        /* throw new UsernameNotFoundException("Votre compte est désactivé"); */
      }

    } else {
      throw new UsernameNotFoundException("Utilisateur non trouvé");
    }

  }
}
