package org.example.service.interfaces;

import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Transaction save(Transaction transaction);
    List<Transaction> findAllOrderByCreatedAtDesc();
    Transaction findById(int id);
    List<Transaction> findByCustomerIdOrderByCreatedAtDesc(int customerId);
    List<Transaction> findByStatusOrderByCreatedAtDesc(TxStatus txStatus);
    List<Transaction> findByPerformedByUserId(int userId);
    List<Transaction> findByApprovedByUserId(int userId);
    List<Transaction> findByCurrencyIdAndCreatedAtBetween(int currencyId,
                                                          LocalDateTime start,
                                                          LocalDateTime end);

    List<Transaction> findByStatusAndCreatedAtBetween(TxStatus status,
                                                      LocalDateTime start,
                                                      LocalDateTime end);

    BigDecimal sumAmountTomanByTypeAndStatusAndCreatedAtBetween(
            TxType type,
            TxStatus status,
            LocalDateTime start,
            LocalDateTime end);

    boolean existsByCustomerIdAndCurrencyIdAndStatus(
            int customerId,
            int currencyId,
            TxStatus status);

    void approveTransaction(int transactionId, int approvedByUserId);
    void rejectTransaction(int transactionId);
}
