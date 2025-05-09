
package bi.gov.obr.informationServices.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity

@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id

  @GeneratedValue(strategy = GenerationType.IDENTITY)

  private Long id;

  @Column(nullable = false)

  @NotBlank(message = "Prénom obligatoire")

  @Size(min = 3, message = "Au moins 3 caractères requis pour le prénom")
  private String firstName;

  @Column(nullable = false)

  @NotBlank(message = "Nom obligatoire")
  private String lastName;

  @Column(nullable = false, unique = true)

  @NotBlank(message = "Email obligatoire")

  @Email(message = "Email invalide")
  private String email;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id")
  private Role role;
  /* private Role roleId; */

  @Column(nullable = false)

  private String password; // Unidirectional mapping is used here // CASCADE is DEFINED TO"NONE"IF ROLE
  private int enabled;

  public User(int enabled) {
    this.enabled = enabled;
  }

  public User(String email) {
    this.email = email;
  }

  public User(String email, String password, Role role) {
    this.email=email;
    this.password=password;
    this.role=role;
  }

  @Override
  public String toString() {
    return "User Email=['" + email + "']";
  }
}


