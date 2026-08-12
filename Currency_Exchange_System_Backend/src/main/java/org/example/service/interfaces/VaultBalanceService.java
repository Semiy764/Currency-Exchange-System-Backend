package org.example.service.interfaces;

import org.example.model.VaultBalance;

import java.math.BigDecimal;
import java.util.List;

public interface VaultBalanceService {

    VaultBalance getBalance(int currencyId);
    List<VaultBalance> getAllBalances();
    List<VaultBalance> getLowBalances(BigDecimal threshold);
    void deposit(int currencyId, BigDecimal amount, int performedByUserId);
}
