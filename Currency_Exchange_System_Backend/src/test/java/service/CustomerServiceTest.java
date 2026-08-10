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

    @Test
    public void findCustomerByNationalIdTest() {

        Customer customer = customerService.findByNationalId("031346420");

        if(customer != null) {
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
    public void updateCustomerTest() {

        Customer customer = customerService.findById(1);
        customer.setFullname("Erfan Simiyari new");
        customer.setNationalId("0313464201");
        customer.setPhoneNumber("09107855810");

        customerService.updateCustomer(1, customer);

        Customer foundCustomeer = customerService.findById(1);
        assertThat(foundCustomeer.getId()).isEqualTo(1);
        assertThat(foundCustomeer.getUserId()).isEqualTo(1);
        assertThat(foundCustomeer.getFullname()).isEqualTo("Erfan Simiyari new");
        assertThat(foundCustomeer.getNationalId()).isEqualTo("0313464201");
        assertThat(foundCustomeer.getPhoneNumber()).isEqualTo("09107855810");

    }

    @Test
    public void isCustomerActiveTest() {

        System.out.println(customerService.isActive(1));
    }
}
