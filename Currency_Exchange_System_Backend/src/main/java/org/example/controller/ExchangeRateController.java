package org.example.controller;

import org.example.dto.request.ExchangeRateRequest;
import org.example.model.ExchangeRate;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.ExchangeRateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rates")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public List<ExchangeRate> allCurrentRates() {
        return exchangeRateService.getAllCurrentRates();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public ExchangeRate addRate(@AuthenticationPrincipal AuthenticatedUser principal,
                                @RequestBody ExchangeRateRequest request) {
        if (request.getCurrencyId() == null) {
            throw new IllegalArgumentException("Currency id is required");
        }
        return exchangeRateService.setRate(request.getCurrencyId().intValue(), request.getBuyRate(), request.getSellRate(), principal.id());
    }

    @GetMapping("/history/{currencyId}")
    public List<ExchangeRate> getHistoryOfCurrencyRates(@PathVariable int currencyId) {
        return exchangeRateService.getRateHistory(currencyId);
    }
}
