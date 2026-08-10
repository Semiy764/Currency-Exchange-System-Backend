package org.example.service.impl;

import org.example.exception.ResourceNotFoundException;
import org.example.model.Customer;
import org.example.repository.interfaces.CustomerRepository;
import org.example.service.interfaces.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findById(int customerId) {

        Customer customer = customerRepository.findById(customerId);
        if(customer == null) {
            throw new ResourceNotFoundException("customer not found with id: " + customerId);
        }
        return customer;
    }

    @Override
    public Customer findByUserId(int userId) {

        Customer customer = customerRepository.findByUserId(userId);
        if(customer == null) {
            throw new ResourceNotFoundException("customer not found with user id: " + userId);
        }

        return customer;
    }
}
