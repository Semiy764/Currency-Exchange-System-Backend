package Repository;

import org.example.App;
import org.example.model.Customer;
import org.example.repository.interfaces.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void saveCustomerTest() {
        Customer customer = new Customer("Ahmad Mohammadi", "08793838", 5, "049494");
        customerRepository.save(customer);
    }

    @Test
    public void findAllCustomersTest() {
        List<Customer> allCustomers = customerRepository.findAll();
        for(Customer customer : allCustomers) {
            System.out.println(customer.getId() + " - " +
                    customer.getFullname() + " - " +
                    customer.getNationalId() + " - " +
                    customer.getUserId() + " - " +
                    customer.getPhoneNumber()
            );
        }
    }

    @Test
    public void findCustomerByIdTest(){

        Customer customer = customerRepository.findById(2);
        assertThat(customer.getId()).isEqualTo(2);
        assertThat(customer.getFullname()).isEqualTo("Mohammad Simiyari");
        assertThat(customer.getNationalId()).isEqualTo("029292929");
        assertThat(customer.getUserId()).isEqualTo(2);
        assertThat(customer.getPhoneNumber()).isEqualTo("091064736");

    }

    @Test
    public void findCustomerByUserIdTest() {
        Customer customer = customerRepository.findByUserId(6);
        assertThat(customer.getId()).isEqualTo(4);
    }

    @Test
    public void existingByUserIdTest() {
        System.out.println(customerRepository.existsByUserId(234));
    }

    @Test
    public void existingCustomerByIdTest() {
        System.out.println(customerRepository.existsById(7));
    }

}
