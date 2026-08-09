package service;

import org.example.App;
import org.example.enums.UserRole;
import org.example.model.User;
import org.example.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    public void findUserByUserIdTest() {

        User user = userService.findById(1);

        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("erfan.simiyari");
        assertThat(passwordEncoder.matches("Mohammad1022@", user.getPasswordHash())).isEqualTo(true);
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
        assertThat(user.isActive()).isEqualTo(true);
    }

    @Test
    public void findUserByUsernameTest() {

        User user = userService.findByUsername("erfan.simiyari");
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("erfan.simiyari");
        assertThat(passwordEncoder.matches("Mohammad1022@", user.getPasswordHash())).isEqualTo(true);
        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER);
        assertThat(user.isActive()).isEqualTo(true);

    }
}
