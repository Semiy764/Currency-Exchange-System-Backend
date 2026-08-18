package org.example.service.impl;

import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.enums.UserRole;
import org.example.exception.AccessDeniedException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Customer;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.CustomerRepository;
import org.example.repository.interfaces.TransactionRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.TransactionService;
import org.springframework.beans.propertyeditors.CustomMapEditor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    @Override
    public List<Transaction> findByStatusAndCreatedAtBetween(TxStatus status, LocalDateTime start, LocalDateTime end) {

        if(status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

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

        return transactionRepository.findByStatusAndCreatedAtBetween(status, start, end);
    }

    @Override
    public BigDecimal sumAmountTomanByTypeAndStatusAndCreatedAtBetween(TxType type, TxStatus status, LocalDateTime start, LocalDateTime end) {

        if(type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }

        if(status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

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

        return transactionRepository.sumAmountTomanByTypeAndStatusAndCreatedAtBetween(type, status, start, end);

    }

    @Override
    public boolean existsByCustomerIdAndCurrencyIdAndStatus(int customerId, int currencyId, TxStatus status) {

        if(customerId <= 0) {
            throw new IllegalArgumentException("Customer id cannot be negative");

        }

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Currency id cannot be negative");
        }

        if(status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        return transactionRepository.existsByCustomerIdAndCurrencyIdAndStatus(customerId, currencyId, status);
    }

    @Override
    public void approveTransaction(int transactionId, int approvedByUserId) {

        if(transactionId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for transaction id");
        }

        if(approvedByUserId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for approved by user id");
        }

        Transaction transaction = transactionRepository.findById(transactionId);
        if(transaction == null) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + transactionId);
        }

        User user = userRepsitory.findById(approvedByUserId);
        if(user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + approvedByUserId);
        }

        if(user.getRole() == UserRole.CUSTOMER) {
            throw new AccessDeniedException("Access denied");
        }

        transactionRepository.approveTransaction(transactionId, approvedByUserId);


    }

    @Override
    public void rejectTransaction(int transactionId, int approvedByUserId) {

        if(transactionId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for transaction id");
        }

        Transaction transaction = transactionRepository.findById(transactionId);
        if(transaction == null) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + transactionId);
        }

        transactionRepository.rejectTransaction(transactionId, approvedByUserId);
    }
}

