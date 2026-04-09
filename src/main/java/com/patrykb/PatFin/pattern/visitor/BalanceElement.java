package com.patrykb.PatFin.pattern.visitor;

import java.math.BigDecimal;

// visitor element 3/3
public class BalanceElement implements MetricElement {
    private final BigDecimal value;

    public BalanceElement(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public void accept(MetricsVisitor visitor) {
        visitor.visit(this);
    }
}
