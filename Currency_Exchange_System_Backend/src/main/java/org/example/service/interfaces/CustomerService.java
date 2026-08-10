package org.example.service.interfaces;

import org.example.model.Customer;

public interface CustomerService {

    Customer findById(int customerId);
//    Customer findByNationalId(String nationalId);
    Customer findByUserId(int userId);
}
