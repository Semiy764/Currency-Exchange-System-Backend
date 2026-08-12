package org.example.service.impl;

import org.example.exception.ResourceNotFoundException;
import org.example.model.VaultLedger;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.VaultLedgerRepository;
import org.example.service.interfaces.VaultLedgerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VaultLedgerServiceImpl implements VaultLedgerService {

    private final VaultLedgerRepository vaultLedgerRepository;
    private final CurrencyRepository currencyRepository;

    public VaultLedgerServiceImpl(VaultLedgerRepository vaultLedgerRepository, CurrencyRepository currencyRepository) {
        this.vaultLedgerRepository = vaultLedgerRepository;
        this.currencyRepository = currencyRepository;
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
}
