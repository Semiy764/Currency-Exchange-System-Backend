package org.example.dto.request;

import java.math.BigDecimal;

public class DepositAndWithdrawRequest {

    private int currencyId;
    private BigDecimal amount;
    private int performedByUserId;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public int getPerformedByUserId() {
        return performedByUserId;
    }

    public void setPerformedByUserId(int performedByUserId) {
        this.performedByUserId = performedByUserId;
    }
}
