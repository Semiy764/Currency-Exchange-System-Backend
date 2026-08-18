package org.example.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProfitLossDtoResponse {
    private LocalDateTime periodStart;
    private LocalDateTime perionEnd;
    private BigDecimal totalBuyAmount;
    private BigDecimal totalSellAmount;
    private BigDecimal profit;

    public ProfitLossDtoResponse() {}

    public ProfitLossDtoResponse(LocalDateTime periodStart,
                                 LocalDateTime perionEnd,
                                 BigDecimal profit,
                                 BigDecimal totalBuyAmount,
                                 BigDecimal totalSellAmount) {
        this.periodStart = periodStart;
        this.perionEnd = perionEnd;
        this.profit = profit;
        this.totalBuyAmount = totalBuyAmount;
        this.totalSellAmount = totalSellAmount;
    }

    public BigDecimal getTotalSellAmount() {
        return totalSellAmount;
    }

    public void setTotalSellAmount(BigDecimal totalSellAmount) {
        this.totalSellAmount = totalSellAmount;
    }

    public BigDecimal getTotalBuyAmount() {
        return totalBuyAmount;
    }

    public void setTotalBuyAmount(BigDecimal totalBuyAmount) {
        this.totalBuyAmount = totalBuyAmount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public LocalDateTime getPerionEnd() {
        return perionEnd;
    }

    public void setPerionEnd(LocalDateTime perionEnd) {
        this.perionEnd = perionEnd;
    }

    public LocalDateTime getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDateTime periodStart) {
        this.periodStart = periodStart;
    }
}
