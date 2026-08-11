package org.example.service.impl;

import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Currency;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.example.service.interfaces.CurrencyService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class CurrencyServiceImpl implements CurrencyService {


    private final CurrencyRepository currencyRepository;
    private final VaultBalanceRepository vaultBalanceRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, VaultBalanceRepository vaultBalanceRepository) {
        this.currencyRepository = currencyRepository;
        this.vaultBalanceRepository = vaultBalanceRepository;
    }


    @Override
    public Currency addCurrency(Currency currency) {

        if(currency == null) {
            throw new IllegalArgumentException("Currency can not be null");
        }

        if(currencyRepository.existsByName(currency.getName())) {
            throw new DuplicateResourceException("This currency name already has been saved: " + currency.getName());
        }

        if(currencyRepository.existsByCode(currency.getCode())) {
            throw new DuplicateResourceException("This currency code already has been saved: " + currency.getCode());
        }

        Currency saved = currencyRepository.save(currency);
        VaultBalance vaultBalance = new VaultBalance(new BigDecimal(0), saved.getId(), LocalDateTime.now());
        vaultBalanceRepository.save(vaultBalance);
        return saved;

    }

    @Override
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }
}
