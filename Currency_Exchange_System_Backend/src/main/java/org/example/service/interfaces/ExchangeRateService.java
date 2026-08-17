package org.example.service.interfaces;

import org.example.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateService {

    ExchangeRate setRate(int currencyId, BigDecimal buyRate, BigDecimal sellRate, int createdByUserId);
    ExchangeRate getCurrentRate(int currencyId);
    List<ExchangeRate> getAllCurrentRates();
}
