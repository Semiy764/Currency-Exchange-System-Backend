package org.example.repository.interfaces;

import org.example.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRatesRepository {

    ExchangeRate save(ExchangeRate exchangeRate);
    List<ExchangeRate> findAll();
    ExchangeRate findLastRateToday(int currencyId);
    List<ExchangeRate> findAllRatesOfCurrency(int currencyId);
    List<ExchangeRate> findAllCurrencyRatesToday(int currencyId);
    void delete(int exchangeRateId);
}
