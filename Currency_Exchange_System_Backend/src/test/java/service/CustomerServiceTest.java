package service;

import org.example.App;
import org.example.enums.UserRole;
import org.example.model.Customer;
import org.example.service.interfaces.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void findCustomerByIdTest() {

        Customer customer = customerService.findById(1);

        assertThat(customer.getId()).isEqualTo(1);
        assertThat(customer.getUserId()).isEqualTo(1);
        assertThat(customer.getFullname()).isEqualTo("Erfan Simiyari");
        assertThat(customer.getNationalId()).isEqualTo("0313464200");
        assertThat(customer.getPhoneNumber()).isEqualTo("09107855814");
    }

    @Test
    public void findCustomerByUserIdTest() {

        Customer customer = customerService.findByUserId(1);

        assertThat(customer.getId()).isEqualTo(1);
        assertThat(customer.getUserId()).isEqualTo(1);
        assertThat(customer.getFullname()).isEqualTo("Erfan Simiyari");
        assertThat(customer.getNationalId()).isEqualTo("0313464200");
        assertThat(customer.getPhoneNumber()).isEqualTo("09107855814");
    }

    @Test
    public void findAllCustomersTest() {

        List<Customer> allCustomers = customerService.findAll();
        for(Customer customer : allCustomers) {
            System.out.println(
                    customer.getId() + " - " +
                    customer.getUserId() + " - " +
                    customer.getPhoneNumber() + " - " +
                    customer.getFullname() + " - " +
                    customer.getNationalId()
                    );
        }
    }

    @Test
    public void existingByNationalIdTest() {
        System.out.println(customerService.existsByNationalId("0313464200"));
    }

    @Test
    public void searchCustomerByNameTest() {

        List<Customer> foundCustomers = customerService.searchByName("Erf");
        for(Customer customer : foundCustomers) {
            System.out.println(
                    customer.getId() + " - " +
                            customer.getUserId() + " - " +
                            customer.getPhoneNumber() + " - " +
                            customer.getFullname() + " - " +
                            customer.getNationalId()
            );
        }
    }
}
