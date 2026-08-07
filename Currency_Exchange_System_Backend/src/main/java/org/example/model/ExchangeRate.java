package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExchangeRate {

    private long id;
    private long currencyId;
    private BigDecimal buyRate;
    private BigDecimal sellRate;
    private LocalDateTime effectiveDate;
    private long createdBy;   // userId

    public ExchangeRate() {}

    public ExchangeRate(BigDecimal buyRate, BigDecimal sellRate, long createdBy, LocalDateTime effectiveDate, long currencyId, long id) {
        this.buyRate = buyRate;
        this.sellRate = sellRate;
        this.createdBy = createdBy;
        this.effectiveDate = effectiveDate;
        this.currencyId = currencyId;
        this.id = id;
    }

    public ExchangeRate(BigDecimal buyRate, BigDecimal sellRate, long createdBy, LocalDateTime effectiveDate, long currencyId) {
        this.buyRate = buyRate;
        this.sellRate = sellRate;
        this.createdBy = createdBy;
        this.effectiveDate = effectiveDate;
        this.currencyId = currencyId;
    }

    public LocalDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public BigDecimal getsellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
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

}
