package com.patrykb.PatFin.pattern.strategy;

import java.math.BigDecimal;

public class MetricContext {
    private MetricStrategy strategy;

    public void setStrategy(MetricStrategy strategy) {
        this.strategy = strategy;
    }

    public BigDecimal calculate(BigDecimal income, BigDecimal expense) {
        return strategy.calculate(income, expense);
    }

    public String getStrategyName() {
        return strategy.name();
    }
}
