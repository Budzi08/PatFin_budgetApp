package com.patrykb.PatFin.pattern.visitor;

public interface MetricsVisitor {
    void visit(IncomeElement element);

    void visit(ExpenseElement element);

    void visit(BalanceElement element);

    String result();
}
