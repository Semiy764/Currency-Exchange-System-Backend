package org.example.controller;

import org.example.model.VaultBalance;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.VaultBalanceService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/vault")
public class VaultBalanceController {

    private final VaultBalanceService vaultBalanceService;

    public VaultBalanceController(VaultBalanceService vaultBalanceService) {
        this.vaultBalanceService = vaultBalanceService;
    }

    @GetMapping("/balances")
    public List<VaultBalance> getAllBalances(@AuthenticationPrincipal AuthenticatedUser principal) {
        isAdminOrTeller(principal);
        return vaultBalanceService.getAllBalances();
    }

    @GetMapping("/balances/{id}")
    public VaultBalance getCurrencyVaultBalance(@AuthenticationPrincipal AuthenticatedUser principal,
                                                @PathVariable int id) {
        isAdminOrTeller(principal);
        return vaultBalanceService.getBalance(id);
    }


    private void isAdminOrTeller(AuthenticatedUser principal) {
        if(!"ADMIN".equals(principal.role()) && !"TELLER".equals(principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin or Teller only");
        }
    }
}
