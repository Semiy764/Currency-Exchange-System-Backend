package org.example.dto.request;

import java.math.BigDecimal;

public class CustomerTransactionRequest {

    private Long currencyId;
    private Long customerId;
    private BigDecimal amountCurrency;
    private BigDecimal amountToman;
    private BigDecimal requestedRate;
    private BigDecimal rateUsed;
    private String type;

    public BigDecimal getRequestedRate() {
        return requestedRate;
    }

    public void setRequestedRate(BigDecimal requestedRate) {
        this.requestedRate = requestedRate;
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

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getRateUsed() {
        return rateUsed;
    }

    public void setRateUsed(BigDecimal rateUsed) {
        this.rateUsed = rateUsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
