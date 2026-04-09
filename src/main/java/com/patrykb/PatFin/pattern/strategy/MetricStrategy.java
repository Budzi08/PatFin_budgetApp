package com.patrykb.PatFin.pattern.strategy;

import java.math.BigDecimal;

public interface MetricStrategy {
    String name();

    BigDecimal calculate(BigDecimal income, BigDecimal expense);
}
