package service;

import io.jsonwebtoken.security.Password;
import org.example.App;
import org.example.dto.request.CustomerRegisterRequest;
import org.example.dto.request.RegisterRequest;
import org.example.dto.request.TellerRegisterRequest;
import org.example.enums.UserRole;
import org.example.model.Customer;
import org.example.model.Teller;
import org.example.model.User;
import org.example.repository.interfaces.CustomerRepository;
import org.example.repository.interfaces.TellerRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TellerRepository tellerRepository;

    @Autowired
    private UserRepsitory userRepsitory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void registerUserTest() {

//        CustomerRegisterRequest request = new CustomerRegisterRequest("Erfan Simiyari", "0313464200", "Mohammad1022@", "09107855814", UserRole.CUSTOMER, "erfan.simiyari");
//        User user = authService.register(request);
//        System.out.println(
//                user.getId() + " - " +
//                user.getUsername() + " - " +
//                user.getPasswordHash() + " - " +
//                user.getRole().name() + " - " +
//                user.isActive()
//                );
//
//        Customer customer = customerRepository.findByUserId(1);
//        System.out.println(
//                customer.getId() + " - " +
//                customer.getUserId() + " - " +
//                customer.getFullname() + " - " +
//                customer.getNationalId() + " - " +
//                customer.getPhoneNumber()
//        );
        TellerRegisterRequest request = new TellerRegisterRequest("Sobhan Simiyari", "0314628665", "Mohammad1023@", "09107241801", UserRole.TELLER, "sobhan.simiyari");
        User user = authService.register(request);
        System.out.println(
                user.getId() + " - " +
                user.getUsername() + " - " +
                user.getPasswordHash() + " - " +
                user.getRole().name() + " - " +
                user.isActive()
                );

        Teller teller = tellerRepository.findByUserId(2);

                System.out.println(
                teller.getId() + " - " +
                teller.getUserId() + " - " +
                teller.getFullname() + " - " +
                teller.getNationalId() + " - " +
                teller.getPhoneNumber());
    }



    @Test
    public void testLogin() {

        User user = authService.login("erfan.simiyari", "Mohammad1022@");
        System.out.println(
                user.isActive() + " - " +
                user.getRole() + " - " +
                user.getId() + " - " +
                user.getUsername() + " - " +
                user.getPasswordHash()
        );

    }

    @Test
    public void changePasswordTest() {

        authService.changePassword(1, "Mohammad1026@", "Mohammad1022@");
        User user = userRepsitory.findById(1);

        System.out.println(passwordEncoder.matches("Mohammad1022@", user.getPasswordHash()));
    }


}
