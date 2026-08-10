package org.example.repository.interfaces;

import org.example.model.Customer;
import org.example.model.User;

import java.util.List;

public interface CustomerRepository {
    Customer save(Customer customer);

    Customer update(Customer customer);

    void delete(int customerId);

    Customer findById(int customerId);

    List<Customer> findAll();

    Customer findByUserId(int userId);

    boolean existsByUserId(int userId);

    boolean existsById(int userId);

    boolean existsByPhone(String phone);

    boolean existsByNationalId(String nationalId);

    List<Customer> searchByName(String name);
}
