package org.example.controller;

import org.example.dto.request.ProfitLossDtoRequest;
import org.example.dto.response.ProfitLossDtoResponse;
import org.example.dto.response.VaultSummaryDto;
import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.model.Transaction;
import org.example.service.interfaces.TransactionService;
import org.example.service.interfaces.VaultLedgerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

// agar dar layer controller yek field request body migiri ke faghat pass midish nemikhad check konish --> e.g : return customerService.updateCustomer(id, currency);   // پاس مستقیم، بدون دستکاری
// vali agar az yek field request body dar hamoon method estefade mikoni bayad ghablesh checkesh koni hatman!!!
// e.g : request.getCurrencyId().intValue()      // ← unboxing، می‌تونه NPE بده
//LocalDateTime.parse(request.getStart()) // ← parse، می‌تونه NPE یا DateTimeParseException بده

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final TransactionService transactionService;
    private final VaultLedgerService vaultLedgerService;

    public ReportsController(TransactionService transactionService, VaultLedgerService vaultLedgerService) {
        this.transactionService = transactionService;
        this.vaultLedgerService = vaultLedgerService;
    }

    @GetMapping("/daily")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Transaction> getDailyTransactions() {
        return transactionService.findTodayTransactions();
    }

    @GetMapping("/profit-loss")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public ProfitLossDtoResponse calculateProfitLoss(@RequestBody ProfitLossDtoRequest request) {

        if (request.getStart() == null || request.getEnd() == null) {
            throw new IllegalArgumentException("start and end are required");
        }

        LocalDateTime start;
        LocalDateTime end;
        try {
            start = LocalDateTime.parse(request.getStart());
            end = LocalDateTime.parse(request.getEnd());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }

        BigDecimal sumBuy = transactionService.sumAmountTomanByTypeAndStatusAndCreatedAtBetween(
                TxType.BUY,
                TxStatus.COMPLETED,
                start,
                end
        );

        BigDecimal sumSell = transactionService.sumAmountTomanByTypeAndStatusAndCreatedAtBetween(
                TxType.SELL,
                TxStatus.COMPLETED,
                start,
                end
        );

        ProfitLossDtoResponse response = new ProfitLossDtoResponse();
        response.setPeriodStart(start);
        response.setPerionEnd(end);
        response.setProfit(sumSell.subtract(sumBuy));
        response.setTotalBuyAmount(sumBuy);
        response.setTotalSellAmount(sumSell);

        return response;

    }

    @GetMapping("/vault-summary/{threshold}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public VaultSummaryDto getVaultSummary(@PathVariable BigDecimal threshold) {
        return vaultLedgerService.getVaultSummary(threshold);
    }

}
