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
    void delete(int currencyId);
    boolean existsById(int currencyId);
    boolean existsByName(String name);
    boolean existsByCode(String code);
    Currency update(Currency currency);
    List<Currency> findAllActiveCurrencies();
    void deactivateCurrency(int id);
    void activateCurrency(int id);
    boolean isActive(int currencyId);


}
