package com.patrykb.PatFin.pattern.visitor;

import java.math.BigDecimal;

// visitor 2/3
public class CsvMetricsVisitor implements MetricsVisitor {
    private BigDecimal income = BigDecimal.ZERO;
    private BigDecimal expense = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;

    @Override
    public void visit(IncomeElement element) {
        income = element.value();
    }

    @Override
    public void visit(ExpenseElement element) {
        expense = element.value();
    }

    @Override
    public void visit(BalanceElement element) {
        balance = element.value();
    }

    @Override
    public String result() {
        return "income,expense,balance\\n" + income + "," + expense + "," + balance;
    }
}
