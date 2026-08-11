package org.example.service.impl;

import org.example.exception.ResourceNotFoundException;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.example.service.interfaces.VaultBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VaultBalanceServiceImpl implements VaultBalanceService {


    private final VaultBalanceRepository vaultBalanceRepository;
    private final CurrencyRepository currencyRepository;

    public VaultBalanceServiceImpl(VaultBalanceRepository vaultBalanceRepository, CurrencyRepository currencyRepository) {
        this.vaultBalanceRepository = vaultBalanceRepository;
        this.currencyRepository = currencyRepository;
    }


    @Override
    public VaultBalance getBalance(int currencyId) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for currency id");
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        VaultBalance balance = vaultBalanceRepository.findByCurrencyId(currencyId);

        if(balance == null) {
            throw new ResourceNotFoundException("Balance not found with currency id: " + currencyId);
        }

        return balance;
    }
}
