package org.example.service.interfaces;

import org.example.enums.TxType;
import org.example.model.ExchangeRate;
import org.example.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ExchangeRateService {

    ExchangeRate setRate(int currencyId, BigDecimal buyRate, BigDecimal sellRate, int createdByUserId);
    ExchangeRate getCurrentRate(int currencyId);
    List<ExchangeRate> getAllCurrentRates();
    List<ExchangeRate> getRateHistory(int currencyId);
    List<ExchangeRate> getRateHistoryBetween(int currencyId, LocalDateTime start, LocalDateTime end);
    BigDecimal getRateForTransaction(int currencyId, TxType type);
    boolean isRateSignificantlyDifferent(BigDecimal requestedRate, BigDecimal currentRate, BigDecimal tolerancePercent);

}
