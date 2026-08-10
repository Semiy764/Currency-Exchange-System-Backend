package service;

import org.example.App;
import org.example.enums.UserRole;
import org.example.model.User;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepsitory userRepsitory;


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

    @Test
    public void findAllUsersTest() {
        List<User> allUsers = userService.findAll();
        for(User user : allUsers) {
            System.out.println(
                    user.isActive() + " - " +
                            user.getRole() + " - " +
                            user.getId() + " - " +
                            user.getUsername() + " - " +
                            user.getPasswordHash()
            );
        }
    }

    @Test
    public void findUsersByRoleTest() {

        List<User> users = userService.findByRole(UserRole.TELLER);
        for(User user : users) {
            System.out.println(
                    user.isActive() + " - " +
                            user.getRole() + " - " +
                            user.getId() + " - " +
                            user.getUsername() + " - " +
                            user.getPasswordHash()
            );
        }
    }

    @Test
    public void existingByUsernameTest() {
        System.out.println(userService.existsByUsername("erfan.simiyari2"));
    }

    @Test
    public void updatingUserTest() {

        User foundUser = userRepsitory.findById(1);
        foundUser.setActive(true);
        User user = userService.updateUser(1, foundUser);

        User updatedUser = userRepsitory.findById(1);
        assertThat(updatedUser.isActive()).isEqualTo(true);
    }

    @Test
    public void deactivateUserTest() {

        userService.deactivateUser(1);
    }


}
