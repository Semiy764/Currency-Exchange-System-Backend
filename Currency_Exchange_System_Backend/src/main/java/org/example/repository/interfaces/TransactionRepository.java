package org.example.repository.interfaces;

import org.example.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    Transaction save(Transaction transaction);
    List<Transaction> findAll();
    Transaction findById(int id);
    List<Transaction> findAllByOrderByCreatedAtDesc();
//    List<Transaction> findByCustomerIdOrderByCreatedAtDesc(int customerId);

}
