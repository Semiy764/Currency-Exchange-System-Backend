package Repository;

import org.example.App;
import org.example.enums.UserRole;
import org.example.model.User;
import org.example.repository.interfaces.UserRepsitory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = App.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepsitory userRepsitory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void userSavingTest() {
        String passHash = passwordEncoder.encode("Mohammad1022@");
        System.out.println(passHash);
        User user = new User("erfan.simiyari4", passHash, UserRole.CUSTOMER, true);
        userRepsitory.save(user);
    }

    @Test
    public void findUserByIdTest() {
        User foundUser = userRepsitory.findById(1);
        assertThat(foundUser.getUsername()).isEqualTo("erfan.simiyari");
        assertThat(foundUser.getRole().name()).isEqualTo("CUSTOMER");
        assertThat(foundUser.isActive()).isEqualTo(true);

    }

    @Test
    public void findAllUsersTest() {
        List<User> allUsers = userRepsitory.findAll();
        for(User user : allUsers) {
            System.out.println(user.getId() + " - " + user.getUsername() + " - " + user.getPasswordHash());
        }
    }

    @Test
    public void findByUsernameTest() {
        User foundUser = userRepsitory.findByUsername("erfan.simiyari4");
        assertThat(foundUser.getId()).isEqualTo(4);
        assertThat(foundUser.getRole().name()).isEqualTo("CUSTOMER");
        assertThat(foundUser.isActive()).isEqualTo(true);
        assertThat(passwordEncoder.matches("Mohammad1022@", foundUser.getPasswordHash())).isEqualTo(true);
    }

    @Test
    public void existingUserByUsernameTest() {
        System.out.println(userRepsitory.existsByUsername("erfan.simiyari4"));
    }

    @Test
    public void existingByUserIdTest() {
        System.out.println(userRepsitory.existsById(9));
    }

    @Test
    public void deletingByUserIdTest() {
        userRepsitory.delete(4);
    }

    @Test
    public void testUpdateUser() {
        User user = userRepsitory.findById(3);
        user.setUsername("erfan.simiyari10");
        user.setRole(UserRole.TELLER);
        user.setActive(false);

        userRepsitory.update(user);

        User updatedUser = userRepsitory.findById(3);
        assertThat(updatedUser.getUsername()).isEqualTo("erfan.simiyari10");
        assertThat(updatedUser.getRole().name()).isEqualTo("TELLER");
        assertThat(updatedUser.isActive()).isEqualTo(false);
    }
}
