package bi.gov.obr.informationServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import bi.gov.obr.informationServices.entity.User;

@SpringBootApplication
@EnableCaching
public class INFOSERVApplication extends SpringBootServletInitializer {
  public static void main(String[] args) {

    SpringApplication.run(INFOSERVApplication.class, args);

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    String rawPwd = "123456";
    String encodedPass = bCryptPasswordEncoder.encode(rawPwd);
    System.out.println(" PWD = " + bCryptPasswordEncoder.encode(rawPwd));

    User user = new User();
    user.setEmail("darius.barasukana@obr.gov.bi");
    user.setEnabled(1);
    user.setFirstName("Darius");
    user.setLastName("BARASUKANA");
    user.setPassword(encodedPass);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(INFOSERVApplication.class);
  }

}
