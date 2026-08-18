package org.example.controller;

import org.example.dto.request.DepositAndWithdrawRequest;
import org.example.model.VaultBalance;
import org.example.model.VaultLedger;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.VaultBalanceService;
import org.example.service.interfaces.VaultLedgerService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/vault")
public class VaultBalanceController {

    private final VaultBalanceService vaultBalanceService;
    private final VaultLedgerService vaultLedgerService;

    public VaultBalanceController(VaultBalanceService vaultBalanceService, VaultLedgerService vaultLedgerService) {
        this.vaultBalanceService = vaultBalanceService;
        this.vaultLedgerService = vaultLedgerService;
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

    @GetMapping("/balances/low/{threshold}")
    public List<VaultBalance> getLowBalances(@AuthenticationPrincipal AuthenticatedUser principal,
                                             @PathVariable BigDecimal threshold) {
        isAdminOrTeller(principal);
        return vaultBalanceService.getLowBalances(threshold);
    }

    @PostMapping("/deposit")
    public VaultBalance deposit(@AuthenticationPrincipal AuthenticatedUser principal,
                                @RequestBody DepositAndWithdrawRequest request) {
        isAdmin(principal);
        vaultBalanceService.deposit(
                request.getCurrencyId(),
                request.getAmount(),
                request.getPerformedByUserId()
        );

        return vaultBalanceService.getBalance(request.getCurrencyId());
    }

    @PostMapping("/withdraw") // bayad amount manfi bashe!!!!!!
    public VaultBalance withdraw(@AuthenticationPrincipal AuthenticatedUser principal,
                                @RequestBody DepositAndWithdrawRequest request) {
        isAdmin(principal);
        vaultBalanceService.withdraw(
                request.getCurrencyId(),
                request.getAmount(),
                request.getPerformedByUserId()
        );

        return vaultBalanceService.getBalance(request.getCurrencyId());
    }


    @GetMapping("/ledger/{currencyId}")
    public List<VaultLedger> getCurrencyLedger(@AuthenticationPrincipal AuthenticatedUser principal,
                                               @PathVariable int currencyId) {
        isAdminOrTeller(principal);
        return vaultLedgerService.getHistory(currencyId);
    }

    @GetMapping("/reconcile/{currencyId}")
    public boolean reconcileWithLedger(@AuthenticationPrincipal AuthenticatedUser principal,
                                       @PathVariable int currencyId) {
        isAdmin(principal);
        return vaultBalanceService.reconcile(currencyId);
    }


    private void isAdminOrTeller(AuthenticatedUser principal) {
        if(!"ADMIN".equals(principal.role()) && !"TELLER".equals(principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin or Teller only");
        }
    }

    private void isAdmin(AuthenticatedUser principal) {
        if(!"ADMIN".equals(principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin only");
        }
    }
}
