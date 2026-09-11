package org.example.controller;

import org.example.dto.request.AdminAndTellerTransactionRequest;
import org.example.dto.request.CustomerTransactionRequest;
import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.exception.AccessDeniedException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.Customer;
import org.example.model.Transaction;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.CustomerService;
import org.example.service.interfaces.TransactionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CustomerService customerService;


    public TransactionController(TransactionService transactionService, CustomerService customerService) {
        this.transactionService = transactionService;
        this.customerService = customerService;
    }

    @PostMapping("/buy")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public Transaction saveBuyTransactionByAdminOrTeller(@AuthenticationPrincipal AuthenticatedUser principal,
                                                  @RequestBody AdminAndTellerTransactionRequest request){
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
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public Transaction saveSellTransactionByAdminOrTeller(@AuthenticationPrincipal AuthenticatedUser principal,
                                                         @RequestBody AdminAndTellerTransactionRequest request){
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
    @PreAuthorize("hasRole('ADMIN')")
    public List<Transaction> getAllTransactions() {
        return transactionService.findAllOrderByCreatedAtDesc();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public Transaction getTransaction(@PathVariable int id) {
        return transactionService.findById(id);
    }



    @PostMapping("/request")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Transaction saveTransactionByCustomer(@AuthenticationPrincipal AuthenticatedUser principal,
                                          @RequestBody CustomerTransactionRequest request) {

        Transaction transaction = new Transaction();
        if (request.getType() == null) {
            throw new IllegalArgumentException("Tx type is required");
        }
        TxType txType;
        try {
            txType = TxType.valueOf(request.getType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + request.getType());
        }
        transaction.setTxType(txType);
        transaction.setCurrencyId(request.getCurrencyId());
        Customer customer = customerService.findByUserId(principal.id());
        transaction.setCustomerId(customer.getId());
        transaction.setAmountCurrency(request.getAmountCurrency());
        transaction.setAmountToman(request.getAmountToman());
        transaction.setRequestedRate(request.getRequestedRate());
        transaction.setRateUsed(request.getRateUsed());
        transaction.setRequestedByCustomer(true);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setStatus(TxStatus.PENDING);

        return transactionService.save(transaction);


    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public List<Transaction> getAllPendingTransactions() {
        return transactionService.findByStatusOrderByCreatedAtDesc(TxStatus.PENDING);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public Transaction approveTransactionByAdminOrTeller(@AuthenticationPrincipal AuthenticatedUser principal,
                                                         @PathVariable int id) {
        transactionService.approveTransaction(id, principal.id());

        return transactionService.findById(id);
    }

    @PostMapping("{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'TELLER')")
    public Transaction rejectTransactionByAdminOrTeller(@AuthenticationPrincipal AuthenticatedUser principal,
                                                        @PathVariable int id) {
        transactionService.rejectTransaction(id, principal.id());
        return transactionService.findById(id);
    }

    @PostMapping("{id}/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    public Transaction cancelTransaction(@AuthenticationPrincipal AuthenticatedUser principal,
                                         @PathVariable int id) {

        isTransactionForThisUser(id, principal.id());
        transactionService.cancelTransaction(id);
        return transactionService.findById(id);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Transaction> getMyTransactions(@AuthenticationPrincipal AuthenticatedUser principal) {

        Customer customer = customerService.findByUserId(principal.id());
        return transactionService.findByCustomerIdOrderByCreatedAtDesc(customer.getId().intValue());
    }


    private void isTransactionForThisUser(int transactionId, int userId) {

        if(userId <= 0) {
            throw new IllegalArgumentException("user id must be positive");
        }

        if(transactionId <= 0) {
            throw new IllegalArgumentException("transaction id must be positive");
        }

        Transaction transaction = transactionService.findById(transactionId);

        if(transaction == null) {
            throw new ResourceNotFoundException("transaction not found with ID: " + transactionId);
        }

        Customer customer = customerService.findByUserId(userId);
        if(customer == null) {
            throw new ResourceNotFoundException("user not found with ID: " + userId);
        }

        if(!Objects.equals(transaction.getCustomerId(), customer.getId())) {
            throw new AccessDeniedException("user id in transaction doesn't match with user id in path");
        }
    }
}
