package org.example.service.impl;

import org.example.enums.UserRole;
import org.example.exception.AccessDeniedException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.User;
import org.example.model.VaultLedger;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.repository.interfaces.VaultLedgerRepository;
import org.example.service.interfaces.VaultLedgerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VaultLedgerServiceImpl implements VaultLedgerService {

    private final VaultLedgerRepository vaultLedgerRepository;
    private final CurrencyRepository currencyRepository;
    private final UserRepsitory userRepsitory;

    public VaultLedgerServiceImpl(VaultLedgerRepository vaultLedgerRepository, CurrencyRepository currencyRepository, UserRepsitory userRepsitory) {
        this.vaultLedgerRepository = vaultLedgerRepository;
        this.currencyRepository = currencyRepository;
        this.userRepsitory = userRepsitory;
    }

    @Override
    public VaultLedger recordEntry(VaultLedger vaultLedger) {

        if(vaultLedger == null) {
            throw new IllegalArgumentException("vault ledger cannot be null");
        }

        return vaultLedgerRepository.save(vaultLedger);
    }

    @Override
    public List<VaultLedger> getHistory(int currencyId) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Please Entre a valid number for currency id");
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        return vaultLedgerRepository.findByCurrencyIdOrderByCreatedAtDesc(currencyId);
    }

    @Override
    public List<VaultLedger> getHistoryBetween(int currencyId, LocalDateTime start, LocalDateTime end) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for currency id");
        }

        if(start == null) {
            throw new IllegalArgumentException("start time cannot be null");
        }

        if(end == null) {
            throw new IllegalArgumentException("end time cannot be null");
        }

        if(start.isAfter(end)) {
            throw new IllegalArgumentException("start time is after the end");
        }

        LocalDateTime now = LocalDateTime.now();

        if(start.isAfter(now)) {
            throw new IllegalArgumentException("start time can not be in the future");
        }

        if(!currencyRepository.existsById(currencyId)) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        return vaultLedgerRepository.findByCurrencyIdAndCreatedAtBetween(currencyId, start, end);

    }

    @Override
    public List<VaultLedger> getByPerformedByUser(int userId) {

        if(userId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for user id");
        }

        User user = userRepsitory.findById(userId);

        if(user == null) {
            throw new ResourceNotFoundException("User not found with ID: " + userId);
        }

        UserRole role = user.getRole();


        if(role == UserRole.CUSTOMER) {
            throw new AccessDeniedException("Access denied");
        }

        return vaultLedgerRepository.findByPerformedByUserId(userId);
    }

    @Override
    public BigDecimal sumChangesBetween(int currencyId, LocalDateTime start, LocalDateTime end) {

        if(currencyId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for currency id");
        }

        if(start == null) {
            throw new IllegalArgumentException("Start date cannot be null");
        }

        if(end == null) {
            throw new IllegalArgumentException("End date cannot be null");
        }

        if(start.isAfter(end)) {
            throw new IllegalArgumentException("Start date is after the end");
        }

        LocalDateTime now = LocalDateTime.now();
        if(start.isAfter(now)) {
            throw new IllegalArgumentException("Start date cannot be in future");
        }

        return vaultLedgerRepository.sumChangeAmountBycurrencyIdAndCreatedAtBetween(currencyId, start, end);
    }
}
