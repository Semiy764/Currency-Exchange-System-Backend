package org.example.service.interfaces;

import org.example.model.VaultBalance;

import java.util.List;

public interface VaultBalanceService {

    VaultBalance getBalance(int currencyId);
    List<VaultBalance> getAllBalances();
}
