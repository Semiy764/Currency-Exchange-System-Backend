package org.example.service.interfaces;

import org.example.model.Customer;

import java.util.List;

public interface CustomerService {

    Customer findById(int customerId);
//    Customer findByNationalId(String nationalId);
    Customer findByUserId(int userId);
    List<Customer> findAll();
    boolean existsByNationalId(String nationalId);
    List<Customer> searchByName(String name);
}
