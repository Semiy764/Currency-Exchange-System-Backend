package org.example.repository.interfaces;

import org.example.enums.TxStatus;
import org.example.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository {

    Transaction save(Transaction transaction);
    List<Transaction> findAll();
    Transaction findById(int id);
    List<Transaction> findAllByOrderByCreatedAtDesc();
    List<Transaction> findByCustomerIdOrderByCreatedAtDesc(int customerId);
    List<Transaction> findByStatusOrderByCreatedAtDesc(TxStatus status);
    List<Transaction> findByPreformedByUserId(int userId);
    List<Transaction> findByApprovedByUserId(int userId);
//    List<Transaction> findByCurrencyIdAndCreatedAtBetween(int currencyId, LocalDateTime start, LocalDateTime finish);

}
