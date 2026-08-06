package org.example.repository.interfaces;

import org.example.model.Currency;

import java.util.List;

public interface CurrencyRepository {
    Currency save(Currency currency);
    List<Currency> findAll();
    Currency findById(int currencyId);
    Currency findBySymbol(String symbol);
    Currency findByName(String name);
    Currency findByCode(String code);
//    void delete(int currencyId);

}
