package org.example.repository.interfaces;

import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.model.Transaction;

import java.math.BigDecimal;
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
    List<Transaction> findByCurrencyIdAndCreatedAtBetween(int currencyId, LocalDateTime start, LocalDateTime finish);
    List<Transaction> findByStatusAndCreatedAtBetween(TxStatus status, LocalDateTime start, LocalDateTime finish);
    boolean existsByCustomerIdAndCurrencyIdAndStatus(int customerId, int currencyId, TxStatus status);
    BigDecimal sumAmountTomanByTypeAndStatusAndCreatedAtBetween(TxType type,
                                                                TxStatus status,
                                                                LocalDateTime start,
                                                                LocalDateTime end);

    void approveTransaction(int transactionId, int approvedByUserId);
    void rejectTransaction(int transactionId, int approvedByUserId);
    void cancelTransaction(int transactionId);
    List<Transaction> findTodayTransactions();

}
