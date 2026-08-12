package org.example.service.impl;

import org.example.enums.LedgerReason;
import org.example.enums.UserRole;
import org.example.exception.AccessDeniedException;
import org.example.exception.ResourceNotFoundException;
import org.example.model.User;
import org.example.model.VaultBalance;
import org.example.model.VaultLedger;
import org.example.repository.interfaces.CurrencyRepository;
import org.example.repository.interfaces.UserRepsitory;
import org.example.repository.interfaces.VaultBalanceRepository;
import org.example.service.interfaces.VaultBalanceService;
import org.example.service.interfaces.VaultLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VaultBalanceServiceImpl implements VaultBalanceService {


    private final VaultBalanceRepository vaultBalanceRepository;
    private final CurrencyRepository currencyRepository;
    private final UserRepsitory userRepsitory;
    private final VaultLedgerService vaultLedgerService;

    public VaultBalanceServiceImpl(VaultBalanceRepository vaultBalanceRepository, CurrencyRepository currencyRepository, UserRepsitory userRepsitory, VaultLedgerService vaultLedgerService) {
        this.vaultBalanceRepository = vaultBalanceRepository;
        this.currencyRepository = currencyRepository;
        this.userRepsitory = userRepsitory;
        this.vaultLedgerService = vaultLedgerService;
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
    @Override
    public void deposit(int currencyId, BigDecimal amount, int performedByUserId) {

        if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("deposit amount must be positive");
        }

        if(performedByUserId <= 0) {
            throw new IllegalArgumentException("Please enter a valid number for performed by user id");
        }



        User user = userRepsitory.findById(performedByUserId);

        if(user == null) {
            throw new ResourceNotFoundException("Currency not found with ID: " + currencyId);
        }

        UserRole role = user.getRole();

        if(role == UserRole.CUSTOMER) {
            throw new AccessDeniedException("Access Denied");
        }

        vaultBalanceRepository.adjustBalance(currencyId, amount);
        VaultLedger vaultLedger = new VaultLedger(amount, LocalDateTime.now(), currencyId, performedByUserId, LedgerReason.DEPOSIT);
        vaultLedgerService.recordEntry(vaultLedger);
    }
}
