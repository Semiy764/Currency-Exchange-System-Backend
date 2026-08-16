package org.example.controller;

import org.example.model.Currency;
import org.example.service.interfaces.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;


    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @GetMapping
    public List<Currency> findAllCurrencies() {
        return currencyService.findAll();
    }
}
