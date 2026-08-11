package org.example.service.impl;

import org.example.exception.ResourceNotFoundException;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.example.service.interfaces.VaultBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

    @Override
    public List<VaultBalance> getAllBalances() {
        return vaultBalanceRepository.findAll();
    }

    @Override
    public List<VaultBalance> getLowBalances(BigDecimal threshold) {

        if(threshold == null || threshold.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for threshold");
        }

        return vaultBalanceRepository.findByBalanceLessThan(threshold);
    }
}
