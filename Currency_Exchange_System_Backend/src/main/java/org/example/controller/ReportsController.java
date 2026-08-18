package org.example.controller;

import org.example.model.Transaction;
import org.example.security.AuthenticatedUser;
import org.example.service.interfaces.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final TransactionService transactionService;

    public ReportsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

//    @GetMapping("/daily")
//    public List<Transaction> getDailyTransactions(@AuthenticationPrincipal AuthenticatedUser principal) {
//
//        isAdmin(principal);
//        return transactionService
//    }

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
