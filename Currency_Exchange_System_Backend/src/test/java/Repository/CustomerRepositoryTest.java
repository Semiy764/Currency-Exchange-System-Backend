package Repository;

import org.example.App;
import org.example.model.Customer;
import org.example.repository.interfaces.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = App.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void saveCustomerTest() {
        Customer customer = new Customer("Ahmad Mohammadi", "08793838", 3, "049494");
        customerRepository.save(customer);
    }

    @Test
    public void findAllCustomersTest() {
        List<Customer> allCustomers = customerRepository.findAll();
        for(Customer customer : allCustomers) {
            System.out.println(customer.getUserId() + customer.getFullname());
        }
    }



}
