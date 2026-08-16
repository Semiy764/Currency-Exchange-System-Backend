package org.example.controller;

import org.example.dto.request.CurrencyRequest;
import org.example.model.Currency;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.CurrencyService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping
    public Currency saveCurrency(@AuthenticationPrincipal AuthenticatedUser principal,
                                 @RequestBody CurrencyRequest request) {

        requireAdmin(principal);
        Currency currency = new Currency();
        currency.setCode(request.getCode());
        currency.setActive(true);
        currency.setName(request.getName());
        currency.setSymbol(request.getSymbol());
        return currencyService.addCurrency(currency);

    }


    private void requireAdmin(AuthenticatedUser principal) {
        if(!"ADMIN".equals(principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin only");
        }
    }
 }
