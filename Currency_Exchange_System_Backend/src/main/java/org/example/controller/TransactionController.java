package org.example.controller;

import org.example.dto.request.AdminAndTellerTransactionRequest;
import org.example.dto.request.CustomerTransactionRequest;
import org.example.enums.TxStatus;
import org.example.enums.TxType;
import org.example.model.Customer;
import org.example.model.Transaction;
import org.example.model.User;
import org.example.repository.interfaces.CustomerRepository;
import org.example.repository.interfaces.TransactionRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.TransactionService;
import org.springframework.data.repository.config.ResourceReaderRepositoryPopulatorBeanDefinitionParser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.ReadOnlyFileSystemException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final UserRepsitory userRepsitory;
    private final CustomerRepository customerRepository;


    public TransactionController(TransactionService transactionService, TransactionRepository transactionRepository, CustomerRepository customerRepository, UserRepsitory userRepsitory, CustomerRepository customerRepository1) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.userRepsitory = userRepsitory;
        this.customerRepository = customerRepository1;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "user id must be positive");
        }

        if(transactionId <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "transaction id must be positive");
        }

        Transaction transaction = transactionRepository.findById(transactionId);

        if(transaction == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "transaction not found with ID: " + transactionId);
        }

        Customer customer = customerRepository.findByUserId(userId);
        if(customer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found with ID: " + userId);
        }

        if(!Objects.equals(transaction.getCustomerId(), customer.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user id in transaction doesn't match with user id in path");
        }
    }
}
