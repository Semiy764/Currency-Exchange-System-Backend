package org.example.repository.interfaces;

import org.example.model.ExchangeRate;

import java.util.List;

public interface ExchangeRatesRepository {

    ExchangeRate save(ExchangeRate exchangeRate);
    List<ExchangeRate> findAll();
}
