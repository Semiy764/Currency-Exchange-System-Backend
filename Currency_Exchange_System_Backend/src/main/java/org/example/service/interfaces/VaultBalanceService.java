package org.example.service.interfaces;

import org.example.model.VaultBalance;

public interface VaultBalanceService {

    VaultBalance getBalance(int currencyId);
}
