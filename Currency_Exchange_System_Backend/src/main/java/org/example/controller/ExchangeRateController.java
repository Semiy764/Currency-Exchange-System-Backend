package org.example.controller;

import org.example.dto.request.ExchangeRateRequest;
import org.example.model.ExchangeRate;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.ExchangeRateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
    public ExchangeRate addRate(@AuthenticationPrincipal AuthenticatedUser principal,
                                @RequestBody ExchangeRateRequest request) {
        isAdminOrTeller(principal);
        return exchangeRateService.setRate(request.getCurrencyId().intValue(), request.getBuyRate(), request.getSellRate(), principal.id());
    }



    private void isAdminOrTeller(AuthenticatedUser principal) {

        if(!"ADMIN".equals(principal.role()) && !"TELLER".equals(principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin or Teller only");
        }
    }


}
