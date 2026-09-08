package org.example.service.impl;
import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Customer;
import org.example.repository.interfaces.CustomerRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String PHONE_PATTERN = "^(\\+98|0)9\\d{9}$";
    private static final String NATIONAL_ID_PATTERN = "^\\d{10}$";

    private final CustomerRepository customerRepository;
    private final UserRepsitory userRepsitory;

    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepsitory userRepsitory) {
        this.customerRepository = customerRepository;
        this.userRepsitory = userRepsitory;
    }

    @Override
    public Customer findById(int customerId) {

        if (customerId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for customer id");
        }
        Customer customer = customerRepository.findById(customerId);
        if(customer == null) {
            throw new ResourceNotFoundException("customer not found with id: " + customerId);
        }
        return customer;
    }

    @Override
    public Customer findByUserId(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for user id");
        }
        Customer customer = customerRepository.findByUserId(userId);
        if(customer == null) {
            throw new ResourceNotFoundException("customer not found with user id: " + userId);
        }
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public boolean existsByNationalId(String nationalId) {

        if (nationalId == null || nationalId.isBlank()) {
            throw new IllegalArgumentException("Enter a valid value for national id");
        }
        return customerRepository.existsByNationalId(nationalId);
    }

    @Override
    public List<Customer> searchByName(String name) {

        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Enter a valid value for name");
        }
        String normalizedName = name.trim();
        return customerRepository.searchByName(normalizedName);
    }

    @Override
    public Customer findByNationalId(String nationalId) {

        if(nationalId == null || nationalId.isBlank()) {
            throw new IllegalArgumentException("Enter a valid value for national id");
        }

        Customer customer = customerRepository.findByNationalId(nationalId);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer not found with national id: " + nationalId);
        }
        return customer;
    }

    @Override
    @Transactional
    public Customer updateCustomer(int customerId, Customer customer) {

        if (customerId <= 0) {
            throw new IllegalArgumentException("Currency id must be positive");
        }

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }

        if (customer.getId() == null || customer.getId().intValue() != customerId) {
            throw new IllegalArgumentException("Customer id in path does not matches customer id in request body");
        }

        Customer foundCustomer = customerRepository.findById(customerId);
        if (foundCustomer == null) {
            throw new ResourceNotFoundException("Customer not found with ID: " + customerId);
        }

        if (customer.getFullname() == null || customer.getFullname().isBlank()) {
            throw new IllegalArgumentException("Customer full name is required");
        }

        customer.setFullname(customer.getFullname().trim());
        if (customer.getFullname().length() > 30) {
            throw new IllegalArgumentException("Customer  name must not exceed 30 characters");
        }

        if (customer.getNationalId() == null || customer.getNationalId().isBlank()) {
            throw new IllegalArgumentException("Customer national id is required");
        }

        if (customer.getUserId() == null || customer.getUserId() <= 0) {
            throw new IllegalArgumentException("Enter a valid number for user id");
        }

        if (customer.getPhoneNumber() == null || customer.getPhoneNumber().isBlank()) {
            throw new IllegalArgumentException("Please enter a valid phone number");
        }


        customer.setPhoneNumber(customer.getPhoneNumber().trim());
        customer.setNationalId(customer.getNationalId().trim());

        if (!customer.getPhoneNumber().matches(PHONE_PATTERN)) {
            throw new IllegalArgumentException("Phone number does not matches the pattern");
        }

        if (!customer.getNationalId().matches(NATIONAL_ID_PATTERN)) {
            throw new IllegalArgumentException("National id must be a 10-digit number");
        }

        int userId = customer.getUserId().intValue();
        if (!userRepsitory.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        Customer existingByUserId = customerRepository.findByUserId(customer.getUserId().intValue());
        if (existingByUserId != null && existingByUserId.getId() != null &&
                existingByUserId.getId().intValue() != customerId) {
            throw new DuplicateResourceException("This user id already has been associated to this customer");
        }

        Customer existingByPhoneNumber = customerRepository.findByPhone(customer.getPhoneNumber());
        if (existingByPhoneNumber != null && existingByPhoneNumber.getId() != null &&
                existingByPhoneNumber.getId().intValue() != customerId) {
            throw new DuplicateResourceException("This phone number has been saved");
        }

        Customer existingByNationalId = customerRepository.findByNationalId(customer.getNationalId());
        if (existingByNationalId != null && existingByNationalId.getId() != null &&
                existingByNationalId.getId().intValue() != customerId) {
            throw new DuplicateResourceException("This national id has already been saved");
        }

        return customerRepository.update(customer);
    }

    @Override
    public boolean isActive(int customerId) {

        if(customerId <= 0) {
            throw new IllegalArgumentException("Enter a valid number for customer id");
        }

        Customer customer = customerRepository.findById(customerId);
        if(customer == null) {
            throw new ResourceNotFoundException("customer not found with id: " + customerId);
        }

        return userRepsitory.isActive(customer.getUserId().intValue());
    }
}
