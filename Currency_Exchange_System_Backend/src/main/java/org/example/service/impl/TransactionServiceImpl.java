package org.example.service.impl;

import org.example.enums.TxStatus;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Customer;
import org.example.model.Transaction;
import org.example.repository.interfaces.CustomerRepository;
import org.example.repository.interfaces.TransactionRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.TransactionService;
import org.springframework.beans.propertyeditors.CustomMapEditor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;
    private final UserRepsitory userRepsitory;
    private final CustomerRepository customerRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepsitory userRepsitory, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepsitory = userRepsitory;
        this.customerRepository = customerRepository;
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

        return transactionRepository.findByPreformedByUserId(userId);
    }
}
