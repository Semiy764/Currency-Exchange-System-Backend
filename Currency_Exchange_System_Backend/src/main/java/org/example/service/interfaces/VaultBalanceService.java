package org.example.service.interfaces;

import org.example.model.VaultBalance;

import java.math.BigDecimal;
import java.util.List;

public interface VaultBalanceService {

    VaultBalance getBalance(int currencyId);
    List<VaultBalance> getAllBalances();
    List<VaultBalance> getLowBalances(BigDecimal threshold);
    void deposit(int currencyId, BigDecimal amount, int performedByUserId);
    void withdraw(int currencyId, BigDecimal amount, int performedByUserId);
    void increaseForApprovedTransaction(int currencyId, BigDecimal amount, int transactionId);
    void decreaseForApprovedTransaction(int currencyId, BigDecimal amount, int transactionId);

    // in 2 method dar approveTransaction estefade mishavand!!!!
    // avali baraya buy va dovomi baraye sell ast az didgahe sarafi !!!!

}
