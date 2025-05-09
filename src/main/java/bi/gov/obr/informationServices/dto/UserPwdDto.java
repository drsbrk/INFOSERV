package bi.gov.obr.informationServices.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPwdDto {
  /*
   * @NotBlank private String oldPassword;
   */
  @NotBlank
  private String newPassword;
  @NotBlank
  private String confNewPassword;
  @NotBlank
  private String email;
}
