package org.example.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class VaultSummaryDto {

    private LocalDateTime generatedAt;
    private List<CurrencyBalanceDto> balances;
    private int lowBalanceCount;

    public List<CurrencyBalanceDto> getBalances() {
        return balances;
    }

    public void setBalances(List<CurrencyBalanceDto> balances) {
        this.balances = balances;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public int getLowBalanceCount() {
        return lowBalanceCount;
    }

    public void setLowBalanceCount(int lowBalanceCount) {
        this.lowBalanceCount = lowBalanceCount;
    }
}
