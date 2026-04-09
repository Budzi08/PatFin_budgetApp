package com.patrykb.PatFin.pattern.visitor;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

// visitor 1/3
public class JsonMetricsVisitor implements MetricsVisitor {
    private final Map<String, BigDecimal> values = new LinkedHashMap<>();

    @Override
    public void visit(IncomeElement element) {
        values.put("income", element.value());
    }

    @Override
    public void visit(ExpenseElement element) {
        values.put("expense", element.value());
    }

    @Override
    public void visit(BalanceElement element) {
        values.put("balance", element.value());
    }

    @Override
    public String result() {
        return values.toString();
    }
}
