package com.patrykb.PatFin.pattern.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BudgetGroup implements BudgetItem {
    private final List<BudgetItem> items = new ArrayList<>();

    public void add(BudgetItem item) {
        items.add(item);
    }

    @Override
    public BigDecimal getBudgetLimit() {
        return items.stream()
                .map(BudgetItem::getBudgetLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}