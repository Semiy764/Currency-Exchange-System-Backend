package org.example.service.impl;

import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Currency;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.example.service.interfaces.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public List<Currency> findAllActiveCurrencies() {
        return currencyRepository.findAllActiveCurrencies();
    }

    @Override
    public boolean existsByCode(String code) {

        if(code == null || code.isBlank()) {
            throw new IllegalArgumentException("Please enter a valid code");
        }
        return currencyRepository.existsByCode(code);
    }

    @Override
    public void deactivateCurrency(int id) {

        if(id <= 0) {
            throw new IllegalArgumentException("Enter a valid number fo id");
        }
        if(!currencyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + id);
        }
        currencyRepository.deactivateCurrency(id);
    }

    @Override
    public void activateCurrency(int id) {

        if(id <= 0) {
            throw new IllegalArgumentException("Enter a valid number fo id");
        }

        if(!currencyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + id);
        }

        currencyRepository.activateCurrency(id);
    }
}
