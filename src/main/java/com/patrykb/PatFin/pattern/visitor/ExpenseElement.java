package com.patrykb.PatFin.pattern.visitor;

import java.math.BigDecimal;

// visitor element 2/3
public class ExpenseElement implements MetricElement {
    private final BigDecimal value;

    public ExpenseElement(BigDecimal value) {
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
