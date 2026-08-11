package org.example.service.interfaces;

import org.example.model.Currency;

import java.util.List;

public interface CurrencyService {

    Currency addCurrency(Currency currency);
    List<Currency> findAll();
    List<Currency> findAllActiveCurrencies();
    boolean existsByCode(String code);
    void deactivateCurrency(int id);
    void activateCurrency(int id);
    Currency updateCurrency(int currencyId, Currency currency);
    Currency findByCode(String code);


    // we have not method for delete currency, we deactivate the currency!!

}
