package org.example.service.impl;

import org.example.enums.TxStatus;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Customer;
import org.example.model.Transaction;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.CustomerRepository;
import org.example.repository.interfaces.TransactionRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.TransactionService;
import org.springframework.beans.propertyeditors.CustomMapEditor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;
    private final UserRepsitory userRepsitory;
    private final CustomerRepository customerRepository;
    private final CurrencyRepository currencyRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepsitory userRepsitory, CustomerRepository customerRepository, CurrencyRepository currencyRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepsitory = userRepsitory;
        this.customerRepository = customerRepository;
        this.currencyRepository = currencyRepository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        if(transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> findAllOrderByCreatedAtDesc() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction findById(int id) {

        if(id <= 0) {
            throw  new IllegalArgumentException("Please enter a valid number for id");
        }
        return transactionRepository.findById(id);

    }

    @Override
    public List<Transaction> findByCustomerIdOrderByCreatedAtDesc(int customerId) {

        if(customerId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for customer id");
        }

        if(!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with ID: " + customerId);
        }

        return transactionRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);
    }

    @Override
    public List<Transaction> findByStatusOrderByCreatedAtDesc(TxStatus txStatus) {

        if(txStatus == null) {
            throw new IllegalArgumentException("txStatus cannot be null");
        }
        return transactionRepository.findByStatusOrderByCreatedAtDesc(txStatus);
    }

    @Override
    public List<Transaction> findByPerformedByUserId(int userId) {

        if(userId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for user id");
        }

        if(!userRepsitory.existsById(userId)) {
            throw new ResourceNotFoundException("user not found with ID: " + userId);
        }

        return transactionRepository.findByPreformedByUserId(userId);
    }

    @Override
    public List<Transaction> findByApprovedByUserId(int userId) {

        if(userId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for user id");
        }

        if(!userRepsitory.existsById(userId)) {
            throw new ResourceNotFoundException("user not found with ID: " + userId);
        }

        return transactionRepository.findByApprovedByUserId(userId);
    }

    @Override
    public List<Transaction> findByCurrencyIdAndCreatedAtBetween(int currencyId, LocalDateTime start, LocalDateTime end) {

        if(start == null) {
            throw new IllegalArgumentException("start cannot be null");
        }

        if(end == null) {
            throw new IllegalArgumentException("end cannot be null");
        }

        if(start.isAfter(end)) {
            throw new IllegalArgumentException("start cannot be after the end");
        }

        if(start.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start cannot be in future");
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        return transactionRepository.findByCurrencyIdAndCreatedAtBetween(currencyId, start, end);
    }
}
