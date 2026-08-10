package org.example.service.impl;

import org.example.exception.ResourceNotFoundException;
import org.example.model.Customer;
import org.example.repository.interfaces.CustomerRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.CustomerService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepsitory userRepsitory;

    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepsitory userRepsitory) {
        this.customerRepository = customerRepository;
        this.userRepsitory = userRepsitory;
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

    @Override
    public List<Customer> findAll() {
        List<Customer> allCustomers = customerRepository.findAll();
        return allCustomers;
    }

    @Override
    public boolean existsByNationalId(String nationalId) {
        return customerRepository.existsByNationalId(nationalId);
    }

    @Override
    public List<Customer> searchByName(String name) {

        if(name == null || name.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "pleas enter a valid name for search"
            );
        }

        List<Customer> foundCusomers = customerRepository.searchByName(name);
        return foundCusomers;
    }

    @Override
    public Customer findByNationalId(String nationalId) {

        if(nationalId == null || nationalId.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "please enter a valid national id"
            );
        }

        Customer customer = customerRepository.findByNationalId(nationalId);
        return customer;
    }

    @Override
    public Customer updateCustomer(int customerId, Customer customer) {

        if(customerId != customer.getId()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "customer id in path does not match customer id in request body"
            );

        }

        Customer foundCustomer = customerRepository.findById(customerId);
        if(foundCustomer == null) {
            throw new ResourceNotFoundException("customer not found with id: " + customerId);
        }
        return customerRepository.update(customer);
    }

    @Override
    public boolean isActive(int customerId) {

        if(customerId <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "enter a valid number for customer id"
            );
        }

        Customer customer = customerRepository.findById(customerId);
        if(customer == null) {
            throw new ResourceNotFoundException("customer not found with id: " + customerId);
        }

        return userRepsitory.isActive(customer.getUserId().intValue());
    }
}
