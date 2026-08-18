package org.example.controller;

import org.example.dto.request.ProfitLossDtoRequest;
import org.example.dto.response.ProfitLossDtoResponse;
import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.model.Transaction;
import org.example.repository.interfaces.TransactionRepository;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final TransactionService transactionService;

    public ReportsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/daily")
    public List<Transaction> getDailyTransactions(@AuthenticationPrincipal AuthenticatedUser principal) {

        isAdmin(principal);
        return transactionService.findTodayTransactions();
    }

    @GetMapping("/profit-loss")
    public ProfitLossDtoResponse calculateProfitLoss(@AuthenticationPrincipal AuthenticatedUser principal,
                                                     @RequestBody ProfitLossDtoRequest request) {

        isAdminOrTeller(principal);
        BigDecimal sumBuy = transactionService.sumAmountTomanByTypeAndStatusAndCreatedAtBetween(
                TxType.BUY,
                TxStatus.COMPLETED,
                LocalDateTime.parse(request.getStart()),
                LocalDateTime.parse(request.getEnd())
        );

        BigDecimal sumSell = transactionService.sumAmountTomanByTypeAndStatusAndCreatedAtBetween(
                TxType.SELL,
                TxStatus.COMPLETED,
                LocalDateTime.parse(request.getStart()),
                LocalDateTime.parse(request.getEnd())
        );

        ProfitLossDtoResponse response = new ProfitLossDtoResponse();
        response.setPeriodStart(LocalDateTime.parse(request.getStart()));
        response.setPerionEnd(LocalDateTime.parse(request.getEnd()));
        response.setProfit(sumSell.subtract(sumBuy));
        response.setTotalBuyAmount(sumBuy);
        response.setTotalSellAmount(sumSell);

        return response;

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
