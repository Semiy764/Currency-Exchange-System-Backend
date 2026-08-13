package org.example.service.interfaces;

import org.example.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction save(Transaction transaction);
    List<Transaction> findAllOrderByCreatedAtDesc();
    Transaction findById(int id);
}
