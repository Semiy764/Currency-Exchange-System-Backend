package org.example.service.impl;

import org.example.enums.TxType;
import org.example.enums.UserRole;
import org.example.exception.AccessDeniedException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.ExchangeRate;
import org.example.model.User;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.ExchangeRatesRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.service.interfaces.ExchangeRateService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.lang.module.ResolutionException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRatesRepository exchangeRatesRepository;
    private final CurrencyRepository currencyRepository;
    private final UserRepsitory userRepsitory;


    public ExchangeRateServiceImpl(ExchangeRatesRepository exchangeRatesRepository, CurrencyRepository currencyRepository, UserRepsitory userRepsitory) {
        this.exchangeRatesRepository = exchangeRatesRepository;
        this.currencyRepository = currencyRepository;
        this.userRepsitory = userRepsitory;
    }

    @Override
    public ExchangeRate setRate(int currencyId, BigDecimal buyRate, BigDecimal sellRate, int createdByUserId) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Enter a valid number for currency id");
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResolutionException("Currency not found with ID: " + currencyId);
        }

        if(buyRate == null) {
            throw new IllegalArgumentException("buyrate cannot by null");
        }

        if(sellRate == null) {
            throw new IllegalArgumentException("sell rate cannot be null");
        }

        if(buyRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Enter a valid number for buyrate");

        }

        if(sellRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Enter a valid number for sell rate");
        }

        User user = userRepsitory.findById(createdByUserId);

        if(user == null) {
            throw new ResolutionException("User not found with ID: " + createdByUserId);
        }

        if(user.getRole() == UserRole.CUSTOMER) {
            throw new AccessDeniedException("Access denied");
        }

        ExchangeRate exchangeRate = new ExchangeRate(buyRate, sellRate, createdByUserId, LocalDateTime.now(),currencyId);
        return exchangeRatesRepository.save(exchangeRate);

    }


    @Override
    public ExchangeRate getCurrentRate(int currencyId) {

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        return exchangeRatesRepository.findLastRateToday(currencyId);
    }

    @Override
    public List<ExchangeRate> getAllCurrentRates() {
        return exchangeRatesRepository.findLatestRateForAllCurrencies();
    }

    @Override
    public List<ExchangeRate> getRateHistory(int currencyId) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Enter a valid number for currency id");
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        return exchangeRatesRepository.findAllRatesOfCurrency(currencyId);
    }

    @Override
    public List<ExchangeRate> getRateHistoryBetween(int currencyId, LocalDateTime start, LocalDateTime end) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Enter a valid number for currency id");
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        if(start == null) {
            throw new IllegalArgumentException("Start cannot be null");
        }

        if(end == null) {
            throw new IllegalArgumentException("End cannot be null");
        }

        if(start.isAfter(end)) {
            throw new IllegalArgumentException("Start cannot be after end");
        }

        return exchangeRatesRepository.findByCurrencyIdAndEffectiveDateBetween(currencyId, start, end);
    }

    @Override
    public BigDecimal getRateForTransaction(int currencyId, TxType type) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for currency id");
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        if(type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }

        ExchangeRate rate = exchangeRatesRepository.findLastRateToday(currencyId);
        return type == TxType.BUY ? rate.getBuyRate() : rate.getsellRate();
    }
}
