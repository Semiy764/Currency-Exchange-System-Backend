package org.example.service.impl;

import org.example.enums.LedgerReason;
import org.example.enums.UserRole;
import org.example.exception.AccessDeniedException;
import org.example.exception.InsufficientBalanceException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.*;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.TransactionRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.example.service.interfaces.VaultBalanceService;
import org.example.service.interfaces.VaultLedgerService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VaultBalanceServiceImpl implements VaultBalanceService {


    private final VaultBalanceRepository vaultBalanceRepository;
    private final CurrencyRepository currencyRepository;
    private final UserRepsitory userRepsitory;
    private final VaultLedgerService vaultLedgerService;
    private final TransactionRepository transactionRepository;

    public VaultBalanceServiceImpl(VaultBalanceRepository vaultBalanceRepository, CurrencyRepository currencyRepository, UserRepsitory userRepsitory, VaultLedgerService vaultLedgerService, TransactionRepository transactionRepository) {
        this.vaultBalanceRepository = vaultBalanceRepository;
        this.currencyRepository = currencyRepository;
        this.userRepsitory = userRepsitory;
        this.vaultLedgerService = vaultLedgerService;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public VaultBalance getBalance(int currencyId) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for currency id");
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        VaultBalance balance = vaultBalanceRepository.findByCurrencyId(currencyId);

        if(balance == null) {
            throw new ResourceNotFoundException("Balance not found with currency id: " + currencyId);
        }

        return balance;
    }

    @Override
    public List<VaultBalance> getAllBalances() {
        return vaultBalanceRepository.findAll();
    }

    @Override
    public List<VaultBalance> getLowBalances(BigDecimal threshold) {

        if(threshold == null || threshold.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for threshold");
        }

        return vaultBalanceRepository.findByBalanceLessThan(threshold);
    }

    // deposit : variz kardan
    @Transactional
    @Override
    public void deposit(int currencyId, BigDecimal amount, int performedByUserId) {

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("deposit amount must be positive");
        }

        if(performedByUserId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for performed by user id");
        }

        Currency currency = currencyRepository.findById(currencyId);

        if(currency == null) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        if(!currency.isActive()) {
            throw new AccessDeniedException("Currency is not active");
        }

        User user = userRepsitory.findById(performedByUserId);

        if(user == null) {
            throw new ResourceNotFoundException("user not found with ID: " + performedByUserId);
        }

        UserRole role = user.getRole();

        if(role == UserRole.CUSTOMER) {
            throw new AccessDeniedException("Access Denied");
        }

        vaultBalanceRepository.adjustBalance(currencyId, amount);
        VaultLedger vaultLedger = new VaultLedger(amount, LocalDateTime.now(), currencyId, performedByUserId, LedgerReason.DEPOSIT);
        vaultLedgerService.recordEntry(vaultLedger);
    }

    @Transactional
    @Override
    public void withdraw(int currencyId, BigDecimal amount, int performedByUserId) {

        if(amount == null || amount.compareTo(BigDecimal.ZERO) >= 0) {
            throw new IllegalArgumentException("amount amount must be negative");
        }

        if(performedByUserId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for performed by user id");
        }

        Currency currency = currencyRepository.findById(currencyId);

        if(currency == null) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        if(!currency.isActive()) {
            throw new AccessDeniedException("Currency is not active");
        }

        VaultBalance balance = vaultBalanceRepository.findByCurrencyId(currencyId);

        BigDecimal withdrawalAmount = amount.negate();
        if(balance.getBalance().compareTo(withdrawalAmount) < 0) {
            throw new InsufficientBalanceException("amount is bigger than vault balance");
        }

        User user = userRepsitory.findById(performedByUserId);

        if(user == null) {
            throw new ResourceNotFoundException("user not found with ID: " + performedByUserId);
        }

        UserRole role = user.getRole();

        if(role == UserRole.CUSTOMER) {
            throw new AccessDeniedException("Access Denied");
        }

        vaultBalanceRepository.adjustBalance(currencyId, amount);
        VaultLedger vaultLedger = new VaultLedger(amount, LocalDateTime.now(), currencyId, performedByUserId, LedgerReason.WITHDRAW);
        vaultLedgerService.recordEntry(vaultLedger);

    }

    @Override
    public void increaseForApprovedTransaction(int currencyId, BigDecimal amount, int transactionId) {

        if(transactionId <= 0) {
            throw new IllegalArgumentException("transaction id must be positive");
        }

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Please enter a valid positive amount");
        }

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Currency id cannot be negative");
        }

        Currency currency = currencyRepository.findById(currencyId);
        if(currency == null) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        if(!currency.isActive()) {
            throw new AccessDeniedException("Currency is not active");
        }

        Transaction transaction = transactionRepository.findById(transactionId);

        if(transaction == null) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + transactionId);
        }

        vaultBalanceRepository.adjustBalance(currencyId, amount);
        VaultLedger vaultLedger = new VaultLedger(amount, LocalDateTime.now(), currencyId, transaction.getPerformedByUserId(), LedgerReason.TX_BUY);
        vaultLedgerService.recordEntry(vaultLedger);

    }
}
