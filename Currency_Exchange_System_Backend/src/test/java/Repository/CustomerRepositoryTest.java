package Repository;

import org.example.App;
import org.example.model.Customer;
import org.example.repository.interfaces.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = App.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void saveCustomerTest() {
        Customer customer = new Customer("Ahmad Mohammadi", "08793838", 2, "049494");
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

    @Test
    public void existingCustomerByPhoneNumberTest() {
        System.out.println(customerRepository.existsByPhone("091064735"));
    }

    @Test
    public void testUpateCustomer() {
        Customer customer = customerRepository.findById(2);
        customer.setFullname("Erfan Simiyari");
        customer.setNationalId("0313464200");
        customer.setPhoneNumber("09107241801");

        customerRepository.update(customer);

        Customer foundCustomer = customerRepository.findById(2);
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getId()).isEqualTo(2);
        assertThat(foundCustomer.getFullname()).isEqualTo("Erfan Simiyari");
        assertThat(foundCustomer.getNationalId()).isEqualTo("0313464200");
        assertThat(foundCustomer.getPhoneNumber()).isEqualTo("09107241801");
    }

    @Test
    public void deleteCustomerTest() {
        customerRepository.delete(3);
    }


    @Test
    public void existingCustomerByNationalIdTest() {

        System.out.println(customerRepository.existsByNationalId("0313464200"));

    }

    @Test
    public void searchCustomerByNameTest() {

        List<Customer> customers = customerRepository.searchByName("Erf");
        for(Customer customer : customers) {
            System.out.println(customer.getId() + " - " +
                    customer.getFullname() + " - " +
                    customer.getNationalId() + " - " +
                    customer.getUserId() + " - " +
                    customer.getPhoneNumber()
            );
        }
    }

}
