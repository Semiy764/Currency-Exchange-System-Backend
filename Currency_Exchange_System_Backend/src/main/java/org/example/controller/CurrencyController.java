package org.example.controller;

import org.example.dto.request.CurrencyRequest;
import org.example.model.Currency;
import org.example.service.interfaces.CurrencyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;


    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @GetMapping
    public List<Currency> findAllCurrencies() {
        return currencyService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Currency saveCurrency(@RequestBody CurrencyRequest request) {

        Currency currency = new Currency();
        currency.setCode(request.getCode());
        currency.setActive(true);
        currency.setName(request.getName());
        currency.setSymbol(request.getSymbol());
        return currencyService.addCurrency(currency);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Currency updateCurrency(@RequestBody CurrencyRequest request,
                                   @PathVariable int id) {


        Currency currency = currencyService.findById(id);
        currency.setName(request.getName());
        currency.setCode(request.getCode());
        currency.setSymbol(request.getSymbol());

        return currencyService.updateCurrency(id, currency);

    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public Currency deactivateCurrency(@PathVariable int id) {
        currencyService.deactivateCurrency(id);
        return currencyService.findById(id);
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public Currency activateCurrency(@PathVariable int id) {
        currencyService.activateCurrency(id);
        return currencyService.findById(id);
    }

}
