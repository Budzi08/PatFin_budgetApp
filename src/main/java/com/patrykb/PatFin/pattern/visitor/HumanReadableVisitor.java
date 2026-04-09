package com.patrykb.PatFin.pattern.visitor;

// visitor 3/3
public class HumanReadableVisitor implements MetricsVisitor {
    private final StringBuilder sb = new StringBuilder();

    @Override
    public void visit(IncomeElement element) {
        sb.append("Income=").append(element.value()).append("; ");
    }

    @Override
    public void visit(ExpenseElement element) {
        sb.append("Expense=").append(element.value()).append("; ");
    }

    @Override
    public void visit(BalanceElement element) {
        sb.append("Balance=").append(element.value());
    }

    @Override
    public String result() {
        return sb.toString();
    }
}
