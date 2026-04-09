package com.patrykb.PatFin.pattern.visitor;

import java.math.BigDecimal;

// visitor element 1/3
public class IncomeElement implements MetricElement {
    private final BigDecimal value;

    public IncomeElement(BigDecimal value) {
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
