package org.example.model;

import org.example.enums.TxStatus;
import org.example.enums.TxType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private long id;
    private TxType txType;
    private long currencyId;
    private long customerId;
    private BigDecimal amountCurrency;
    private BigDecimal amountToman;
    private BigDecimal requestedRate;
    private BigDecimal rateUsed;
    private boolean requestedByCustomer;
    private long performedByUserId;
    private long approvedByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private TxStatus status;

    public Transaction(BigDecimal amountCurrency,
                       BigDecimal amountToman,
                       LocalDateTime approvedAt,
                       long approvedByUserId,
                       LocalDateTime createdAt,
                       long currencyId,
                       long customerId,
                       long id,
                       long performedByUserId,
                       boolean requestedByCustomer,
                       BigDecimal rateUsed,
                       BigDecimal requestedRate,
                       TxStatus status,
                       TxType txType) {

        this.amountCurrency = amountCurrency;
        this.amountToman = amountToman;
        this.approvedAt = approvedAt;
        this.approvedByUserId = approvedByUserId;
        this.createdAt = createdAt;
        this.currencyId = currencyId;
        this.customerId = customerId;
        this.id = id;
        this.performedByUserId = performedByUserId;
        this.requestedByCustomer = requestedByCustomer;
        this.rateUsed = rateUsed;
        this.requestedRate = requestedRate;
        this.status = status;
        this.txType = txType;
    }

    public BigDecimal getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(BigDecimal amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    public BigDecimal getAmountToman() {
        return amountToman;
    }

    public void setAmountToman(BigDecimal amountToman) {
        this.amountToman = amountToman;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public long getApprovedByUserId() {
        return approvedByUserId;
    }

    public void setApprovedByUserId(long approvedByUserId) {
        this.approvedByUserId = approvedByUserId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(long currencyId) {
        this.currencyId = currencyId;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPerformedByUserId() {
        return performedByUserId;
    }

    public void setPerformedByUserId(long performedByUserId) {
        this.performedByUserId = performedByUserId;
    }

    public BigDecimal getRateUsed() {
        return rateUsed;
    }

    public void setRateUsed(BigDecimal rateUsed) {
        this.rateUsed = rateUsed;
    }

    public boolean isRequestedByCustomer() {
        return requestedByCustomer;
    }

    public void setRequestedByCustomer(boolean requestedByCustomer) {
        this.requestedByCustomer = requestedByCustomer;
    }

    public BigDecimal getRequestedRate() {
        return requestedRate;
    }

    public void setRequestedRate(BigDecimal requestedRate) {
        this.requestedRate = requestedRate;
    }

    public TxStatus getStatus() {
        return status;
    }

    public void setStatus(TxStatus status) {
        this.status = status;
    }

    public TxType getTxType() {
        return txType;
    }

    public void setTxType(TxType txType) {
        this.txType = txType;
    }
}
