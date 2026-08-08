package org.example.repository.interfaces;

import org.example.model.VaultBalance;

import java.math.BigDecimal;
import java.util.List;

public interface VaultBalanceRepository {

    VaultBalance save(VaultBalance vaultBalance);
    List<VaultBalance> findAll();
    boolean existsByCurrencyId(int currencyId);
    VaultBalance findByCurrencyId(int currencyId);
    void adjustBalance(int currencyId, BigDecimal amount);
    void deleteById(int id);
    VaultBalance findById(int id);
    List<VaultBalance> findByBalanceLessThan(BigDecimal threshold);
}
