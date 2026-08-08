package org.example.model;

import org.example.enums.LedgerReason;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VaultLedger {  // har rooydade taghire mojoodi ro negah midare va hichvaght update ya delete nemishe

    private long id;
    private long currencyId;
    private BigDecimal changeAmount;
    private LedgerReason reason;
    private LocalDateTime createdAt;
    private long preformedByUserId;

    public VaultLedger() {}

    public VaultLedger(BigDecimal changeAmount,
                       LocalDateTime createdAt,
                       long currencyId,
                       long id,
                       long preformedByUserId,
                       LedgerReason reason) {

        this.changeAmount = changeAmount;
        this.createdAt = createdAt;
        this.currencyId = currencyId;
        this.id = id;
        this.preformedByUserId = preformedByUserId;
        this.reason = reason;
    }

    public VaultLedger(BigDecimal changeAmount,
                       LocalDateTime createdAt,
                       long currencyId,
                       long preformedByUserId,
                       LedgerReason reason) {

        this.changeAmount = changeAmount;
        this.createdAt = createdAt;
        this.currencyId = currencyId;
        this.preformedByUserId = preformedByUserId;
        this.reason = reason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getPreformedByUserId() {
        return preformedByUserId;
    }

    public void setPreformedByUserId(long preformedByUserId) {
        this.preformedByUserId = preformedByUserId;
    }

    public LedgerReason getReason() {
        return reason;
    }

    public void setReason(LedgerReason reason) {
        this.reason = reason;
    }
}
