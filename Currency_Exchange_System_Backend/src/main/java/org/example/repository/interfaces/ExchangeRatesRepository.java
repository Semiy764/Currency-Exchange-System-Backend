package org.example.repository.interfaces;

import org.example.model.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRatesRepository {

    ExchangeRate save(ExchangeRate exchangeRate);
    List<ExchangeRate> findAll();
    ExchangeRate findLastRateToday(int currencyId);
    List<ExchangeRate> findAllRatesOfCurrency(int currencyId);
    List<ExchangeRate> findAllCurrencyRatesToday(int currencyId);
    void delete(int exchangeRateId);
    List<ExchangeRate> findByCreatedBy(int userId);  // mige in user che nerkh hayi ro sabt karde
    List<ExchangeRate> findByCurrencyIdAndEffectiveDateBetween(int currencyId,
                                                               LocalDateTime start,
                                                               LocalDateTime end);

    List<ExchangeRate> findLatestRateForAllCurrencies();
}
