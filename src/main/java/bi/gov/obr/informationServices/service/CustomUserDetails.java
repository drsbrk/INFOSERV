package bi.gov.obr.informationServices.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import bi.gov.obr.informationServices.entity.Role;
import bi.gov.obr.informationServices.entity.User;
import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails {
  /**
   *
   */
  private static final long serialVersionUID = 2471481106388916162L;

  private User user;

  public CustomUserDetails(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Role roles = user.getRole();
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    /*
     * for (Role role : roles) { authorities.add(new
     * SimpleGrantedAuthority(role.getName()));
     * authorities.add(Collections.singletonList(user.getRole().getName()))) }
     */

    authorities.add(new SimpleGrantedAuthority(roles.getName()));
    return authorities;
  }

  public String getFullName() {

    return user.getFirstName() + " " + user.getLastName();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  public User getUser() {
    return user;
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  /*
   * public boolean hasRole(String roleName) { return user.hasRole(roleName); }
   */

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    if (user.getEnabled() == 0) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    User user = this.getUser();
    if (user.getEnabled() == 1 || user.getEnabled() == -1) {
      return true;
    }
    return false;
  }

  public void setAuthorities(Set<GrantedAuthority> authorities) {

  }

  public void setUser(User user) {
    this.user = user;
  }
}
