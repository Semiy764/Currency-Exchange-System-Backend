package org.example.controller;

import org.example.dto.request.DepositAndWithdrawRequest;
import org.example.model.VaultBalance;
import org.example.model.VaultLedger;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.VaultBalanceService;
import org.example.service.interfaces.VaultLedgerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public List<VaultBalance> getAllBalances() {
        return vaultBalanceService.getAllBalances();
    }

    @GetMapping("/balances/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public VaultBalance getCurrencyVaultBalance(@PathVariable int id) {
        return vaultBalanceService.getBalance(id);
    }

    @GetMapping("/balances/low/{threshold}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public List<VaultBalance> getLowBalances(@PathVariable BigDecimal threshold) {
        return vaultBalanceService.getLowBalances(threshold);
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('ADMIN')")
    public VaultBalance deposit(@RequestBody DepositAndWithdrawRequest request) {
        vaultBalanceService.deposit(
                request.getCurrencyId(),
                request.getAmount(),
                request.getPerformedByUserId()
        );

        return vaultBalanceService.getBalance(request.getCurrencyId());
    }

    @PostMapping("/withdraw") // bayad amount manfi bashe!!!!!!
    @PreAuthorize("hasRole('ADMIN')")
    public VaultBalance withdraw(@RequestBody DepositAndWithdrawRequest request) {
        vaultBalanceService.withdraw(
                request.getCurrencyId(),
                request.getAmount(),
                request.getPerformedByUserId()
        );

        return vaultBalanceService.getBalance(request.getCurrencyId());
    }


    @GetMapping("/ledger/{currencyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public List<VaultLedger> getCurrencyLedger(@PathVariable int currencyId) {
        return vaultLedgerService.getHistory(currencyId);
    }

    @GetMapping("/reconcile/{currencyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean reconcileWithLedger(@PathVariable int currencyId) {
        return vaultBalanceService.reconcile(currencyId);
    }
}
