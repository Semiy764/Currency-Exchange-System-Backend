package org.example.service.interfaces;

import org.example.model.Currency;

import java.util.List;

public interface CurrencyService {

    Currency addCurrency(Currency currency);
    List<Currency> findAll();
    List<Currency> findAllActiveCurrencies();

}
