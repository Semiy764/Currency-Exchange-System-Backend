package org.example.controller;

import org.example.dto.request.AdminAndTellerTransactionRequest;
import org.example.dto.request.CustomerTransactionRequest;
import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.model.Transaction;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;


    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/buy")
    public Transaction saveBuyTransactionByAdminOrTeller(@AuthenticationPrincipal AuthenticatedUser principal,
                                                  @RequestBody AdminAndTellerTransactionRequest request){
        isAdminOrTeller(principal);
        Transaction transaction = new Transaction();
        transaction.setTxType(TxType.BUY);
        transaction.setCurrencyId(request.getCurrencyId());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setAmountCurrency(request.getAmountCurrency());
        transaction.setAmountToman(request.getAmountToman());
        transaction.setRequestedRate(request.getRequestedRate());
        transaction.setRateUsed(request.getRateUsed());
        transaction.setRequestedByCustomer(false);
        transaction.setPerformedByUserId((long) principal.id());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setStatus(TxStatus.COMPLETED);

        return transactionService.save(transaction);

    }

    @PostMapping("/sell")
    public Transaction saveSellTransactionByAdminOrTeller(@AuthenticationPrincipal AuthenticatedUser principal,
                                                         @RequestBody AdminAndTellerTransactionRequest request){
        isAdminOrTeller(principal);
        Transaction transaction = new Transaction();
        transaction.setTxType(TxType.SELL);
        transaction.setCurrencyId(request.getCurrencyId());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setAmountCurrency(request.getAmountCurrency());
        transaction.setAmountToman(request.getAmountToman());
        transaction.setRequestedRate(request.getRequestedRate());
        transaction.setRateUsed(request.getRateUsed());
        transaction.setRequestedByCustomer(false);
        transaction.setPerformedByUserId((long) principal.id());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setStatus(TxStatus.COMPLETED);

        return transactionService.save(transaction);

    }

    @GetMapping
    public List<Transaction> getAllTransactions(@AuthenticationPrincipal AuthenticatedUser principal) {
        isAdmin(principal);
        return transactionService.findAllOrderByCreatedAtDesc();
    }

    @GetMapping("/{id}")
    public Transaction getTransaction(@PathVariable int id) {
        return transactionService.findById(id);
    }

    @PostMapping("/request")
    public Transaction saveTransactionByCustomer(@AuthenticationPrincipal AuthenticatedUser principal,
                                          @RequestBody CustomerTransactionRequest request) {
        isCustomer(principal);
        Transaction transaction = new Transaction();
        transaction.setTxType(TxType.valueOf(request.getType()));
        transaction.setCurrencyId(request.getCurrencyId());
        transaction.setCustomerId(request.getCustomerId());
        transaction.setAmountCurrency(request.getAmountCurrency());
        transaction.setAmountToman(request.getAmountToman());
        transaction.setRequestedRate(request.getRequestedRate());
        transaction.setRateUsed(request.getRateUsed());
        transaction.setRequestedByCustomer(true);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setStatus(TxStatus.PENDING);

        return transactionService.save(transaction);


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

    private void isCustomer(AuthenticatedUser principal) {
        if(!"CUSTOMER".equals(principal.role())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Customer only");
        }
    }
}
