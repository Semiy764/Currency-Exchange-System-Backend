package org.example.service.interfaces;

import org.example.model.ExchangeRate;

import java.math.BigDecimal;

public interface ExchangeRateService {

    ExchangeRate setRate(int currencyId, BigDecimal buyRate, BigDecimal sellRate, int createdByUserId);
    ExchangeRate getCurrentRate(int currencyId);
}
