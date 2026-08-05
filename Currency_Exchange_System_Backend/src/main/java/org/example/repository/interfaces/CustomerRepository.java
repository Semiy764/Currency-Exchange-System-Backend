package org.example.repository.interfaces;

import org.example.model.Customer;
import org.example.model.User;

import java.util.List;

public interface CustomerRepository {
    Customer save(Customer customer);

//    User update(User user);
//
//    void delete(int userId);
//
//    User findById(int userId);
//
//    User findByUsername(String username);
//
    List<Customer> findAll();
//
//    boolean existsByUsername(String username);
//
//    boolean existsById(int userId);
//
////    boolean existsByPhone(String phone); this is for customer
}
