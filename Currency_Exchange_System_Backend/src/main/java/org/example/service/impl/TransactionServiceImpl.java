package org.example.service.impl;

import org.example.model.Transaction;
import org.example.repository.interfaces.TransactionRepository;
import org.example.service.interfaces.TransactionService;
import org.springframework.stereotype.Service;

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
}
