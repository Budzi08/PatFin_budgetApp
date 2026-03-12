package com.patrykb.PatFin.pattern.composite;

import java.math.BigDecimal;

public class CategoryBudget implements BudgetItem {
    private final BigDecimal limit;

    public CategoryBudget(BigDecimal limit) {
        this.limit = limit;
    }

    @Override
    public BigDecimal getBudgetLimit() {
        return limit;
    }
}