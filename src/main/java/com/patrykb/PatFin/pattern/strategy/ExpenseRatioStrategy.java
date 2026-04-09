package com.patrykb.PatFin.pattern.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

// strategy 3/3
public class ExpenseRatioStrategy implements MetricStrategy {
    @Override
    public String name() {
        return "expenseRatioPercent";
    }

    @Override
    public BigDecimal calculate(BigDecimal income, BigDecimal expense) {
        if (income.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return expense
                .divide(income, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
