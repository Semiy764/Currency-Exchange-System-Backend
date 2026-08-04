package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VaultBalance {

    private long id;
    private long currencyId;
    private BigDecimal balance;
    private LocalDateTime lastUpdated;

    public VaultBalance(BigDecimal balance, long currencyId, long id, LocalDateTime lastUpdated) {
        this.balance = balance;
        this.currencyId = currencyId;
        this.id = id;
        this.lastUpdated = lastUpdated;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
