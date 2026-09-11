package org.example.service.impl;

import org.example.exception.DuplicateResourceException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Currency;
import org.example.model.VaultBalance;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.example.service.interfaces.CurrencyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private static final String CODE_PATTERN = "^[A-Z]{3}$";
    private final CurrencyRepository currencyRepository;
    private final VaultBalanceRepository vaultBalanceRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, VaultBalanceRepository vaultBalanceRepository) {
        this.currencyRepository = currencyRepository;
        this.vaultBalanceRepository = vaultBalanceRepository;
    }


    @Override
    @Transactional
    public Currency addCurrency(Currency currency) {

        if(currency == null) {
            throw new IllegalArgumentException("Currency can not be null");
        }

        if (currency.getName() == null || currency.getName().isBlank()) {
            throw new IllegalArgumentException("Currency name is required");
        }

        if (currency.getCode() == null || currency.getCode().isBlank()) {
            throw new IllegalArgumentException("Currency code is required");
        }

        if (currency.getName().length() > 10) {
            throw new IllegalArgumentException("Currency name must not exceed 10 characters");
        }

        currency.setName(currency.getName().trim());
        currency.setCode(currency.getCode().trim().toUpperCase());

        if (currency.getSymbol() != null) {
            currency.setSymbol(currency.getSymbol().trim());
            if (currency.getSymbol().isBlank()) {
                currency.setSymbol(null);
            } else if (currency.getSymbol().length() > 5) {
                throw new IllegalArgumentException("Currency symbol must not exceed 5 character");
            }
        }

        if (!currency.getCode().matches(CODE_PATTERN)) {
            throw new IllegalArgumentException("Currency code must be a 3-letter ISO code (e.g. USD)");
        }

        if(currencyRepository.existsByName(currency.getName())) {
            throw new DuplicateResourceException("This currency name already has been saved: " + currency.getName());
        }

        if(currencyRepository.existsByCode(currency.getCode())) {
            throw new DuplicateResourceException("This currency code already has been saved: " + currency.getCode());
        }

        Currency saved = currencyRepository.save(currency);
        VaultBalance vaultBalance = new VaultBalance(BigDecimal.ZERO, saved.getId(), LocalDateTime.now());
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
        String normalizedCode = code.trim().toUpperCase();
        return currencyRepository.existsByCode(normalizedCode);
    }

    @Override
    public void deactivateCurrency(int id) {

        if(id <= 0) {
            throw new IllegalArgumentException("Enter a valid number for id");
        }
        if(!currencyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + id);
        }
        currencyRepository.deactivateCurrency(id);
    }

    @Override
    public void activateCurrency(int id) {

        if(id <= 0) {
            throw new IllegalArgumentException("Enter a valid number for id");
        }

        if(!currencyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + id);
        }

        currencyRepository.activateCurrency(id);
    }

    @Override
    @Transactional
    public Currency updateCurrency(int currencyId, Currency currency) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for currencyId");
        }

        if(currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        if(currencyId != currency.getId()) {
            throw new IllegalArgumentException(
                    "currencyId in path does not matches currencyId in request body"
            );
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        if (currency.getName() == null || currency.getName().isBlank()) {
            throw new IllegalArgumentException("Currency name is required");
        }

        if (currency.getName().length() > 10) {
            throw new IllegalArgumentException("Currency name must not exceed 10 characters");
        }

        if (currency.getCode() == null || currency.getCode().isBlank()) {
            throw new IllegalArgumentException("Currency code is required");
        }

        currency.setName(currency.getName().trim());
        currency.setCode(currency.getCode().trim().toUpperCase());

        if (currency.getSymbol() != null) {
            currency.setSymbol(currency.getSymbol().trim());
            if (currency.getSymbol().isBlank()) {
                currency.setSymbol(null);
            } else if (currency.getSymbol().length() > 5) {
                throw new IllegalArgumentException("Currency symbol must not exceed 5 character");
            }
        }

        if (!currency.getCode().matches(CODE_PATTERN)) {
            throw new IllegalArgumentException("Currency code must be a 3-letter ISO code (e.g. USD)");
        }

        Currency existingByCode = currencyRepository.findByCode(currency.getCode());

        if (existingByCode != null && existingByCode.getId() != currencyId) {
            throw new DuplicateResourceException("This currency code already has been saved: " + currency.getCode());
        }

        Currency existingByName = currencyRepository.findByName(currency.getName());

        if (existingByName != null && existingByName.getId() != currencyId) {
            throw new DuplicateResourceException("This currency name already has been saved");
        }

        return currencyRepository.update(currency);
    }

    @Override
    public Currency findByCode(String code) {

        if(code == null || code.isBlank()) {
            throw new IllegalArgumentException("Enter a valid code");
        }

        String normalizedCode = code.trim().toUpperCase();
        Currency currency = currencyRepository.findByCode(normalizedCode);
        if(currency == null) {
            throw new ResourceNotFoundException("Currency not found with code: " + code);
        }
        return currency;
    }

    @Override
    public Currency findById(int currencyId) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for currencyId");
        }

        Currency currency = currencyRepository.findById(currencyId);
        if(currency == null) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        return currency;
    }

    @Override
    public boolean isActive(int currencyId) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for currency id");
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        return currencyRepository.isActive(currencyId);
    }
}
