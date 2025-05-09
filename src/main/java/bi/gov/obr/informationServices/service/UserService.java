package bi.gov.obr.informationServices.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import bi.gov.obr.informationServices.entity.User;

public interface UserService {

  String ariaControls(String input);

  String buildMenu(@AuthenticationPrincipal UserDetails userDetails);



  List<User> findAllUsers();

  User findUserByUsername(String username);

  boolean isOldPasswordValid(User user, String oldPassword);



  boolean isUserChoosingAssignedPwdFromAdmin(String chosenPwd);


  boolean performLogin();

  void putNavMenuIntoSessionAndModel(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request,
      Model model);

  User saveUser(User user);

  void toggleEnabled(String email);
}
