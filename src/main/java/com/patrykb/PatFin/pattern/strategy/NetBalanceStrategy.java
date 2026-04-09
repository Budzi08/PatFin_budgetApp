package com.patrykb.PatFin.pattern.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;

// strategy 1/3
public class NetBalanceStrategy implements MetricStrategy {
    @Override
    public String name() {
        return "netBalance";
    }

    @Override
    public BigDecimal calculate(BigDecimal income, BigDecimal expense) {
        return income.subtract(expense).setScale(2, RoundingMode.HALF_UP);
    }
}
