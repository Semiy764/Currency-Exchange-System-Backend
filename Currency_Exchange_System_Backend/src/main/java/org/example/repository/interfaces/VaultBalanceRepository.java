package org.example.repository.interfaces;

import org.example.model.VaultBalance;

import java.util.List;

public interface VaultBalanceRepository {

    VaultBalance save(VaultBalance vaultBalance);
    List<VaultBalance> findAll();
    boolean existsByCurrencyId(int currencyId);
    VaultBalance findByCurrencyId(int currencyId);
}
