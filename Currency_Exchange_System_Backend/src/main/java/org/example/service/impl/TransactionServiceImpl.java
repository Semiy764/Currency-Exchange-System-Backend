package org.example.service.impl;

import org.example.model.Transaction;
import org.example.repository.interfaces.TransactionRepository;
import org.example.service.interfaces.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {


    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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

}
