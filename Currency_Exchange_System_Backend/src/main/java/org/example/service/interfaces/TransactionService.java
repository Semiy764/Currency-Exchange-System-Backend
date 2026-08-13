package org.example.service.interfaces;

import org.example.enums.TxStatus;
import org.example.model.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction save(Transaction transaction);
    List<Transaction> findAllOrderByCreatedAtDesc();
    Transaction findById(int id);
    List<Transaction> findByCustomerIdOrderByCreatedAtDesc(int customerId);
    List<Transaction> findByStatusOrderByCreatedAtDesc(TxStatus txStatus);
    List<Transaction> findByPerformedByUserId(int userId);
}
