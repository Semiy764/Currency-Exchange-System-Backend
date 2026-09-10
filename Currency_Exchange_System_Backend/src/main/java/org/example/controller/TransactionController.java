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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    public Transaction getTransaction(@PathVariable int id,
                                      @AuthenticationPrincipal AuthenticatedUser principal) {
        isAdminOrTeller(principal);
        return transactionService.findById(id);
    }



    @PostMapping("/request")
    public Transaction saveTransactionByCustomer(@AuthenticationPrincipal AuthenticatedUser principal,
                                          @RequestBody CustomerTransactionRequest request) {
        isCustomer(principal);
        Transaction transaction = new Transaction();
        if (request.getType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tx type is required");
        }
        TxType txType;
        try {
            txType = TxType.valueOf(request.getType());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction type: " + request.getType());
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
    public List<Transaction> getAllPendingTransactions(@AuthenticationPrincipal AuthenticatedUser principal) {
        isAdminOrTeller(principal);
        return transactionService.findByStatusOrderByCreatedAtDesc(TxStatus.PENDING);
    }

    @PostMapping("/{id}/approve")
    public Transaction approveTransactionByAdminOrTeller(@AuthenticationPrincipal AuthenticatedUser principal,
                                                         @PathVariable int id) {
        isAdminOrTeller(principal);
        transactionService.approveTransaction(id, principal.id());

        return transactionService.findById(id);
    }

    @PostMapping("{id}/reject")
    public Transaction rejectTransactionByAdminOrTeller(@AuthenticationPrincipal AuthenticatedUser principal,
                                                        @PathVariable int id) {

        isAdminOrTeller(principal);
        transactionService.rejectTransaction(id, principal.id());
        return transactionService.findById(id);
    }

    @PostMapping("{id}/cancel")
    public Transaction cancelTransaction(@AuthenticationPrincipal AuthenticatedUser principal,
                                         @PathVariable int id) {
        isCustomer(principal);
        isTransactionForThisUser(id, principal.id());
        transactionService.cancelTransaction(id);
        return transactionService.findById(id);
    }

    @GetMapping("/my")
    public List<Transaction> getMyTransactions(@AuthenticationPrincipal AuthenticatedUser principal) {
        isCustomer(principal);
        Customer customer = customerService.findByUserId(principal.id());
        return transactionService.findByCustomerIdOrderByCreatedAtDesc(customer.getId().intValue());
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
