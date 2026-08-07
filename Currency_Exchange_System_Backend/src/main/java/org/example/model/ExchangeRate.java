package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExchangeRate {

    private long id;
    private long currencyId;
    private BigDecimal buyRate;
    private BigDecimal cellRate;
    private LocalDateTime effectiveDate;
    private long createdBy;   // userId

    public ExchangeRate(BigDecimal buyRate, BigDecimal cellRate, long createdBy, LocalDateTime effectiveDate, long currencyId, long id) {
        this.buyRate = buyRate;
        this.cellRate = cellRate;
        this.createdBy = createdBy;
        this.effectiveDate = effectiveDate;
        this.currencyId = currencyId;
        this.id = id;
    }

    public ExchangeRate(BigDecimal buyRate, BigDecimal cellRate, long createdBy, LocalDateTime effectiveDate, long currencyId) {
        this.buyRate = buyRate;
        this.cellRate = cellRate;
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
        return cellRate;
    }

    public void setCellRate(BigDecimal cellRate) {
        this.cellRate = cellRate;
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
