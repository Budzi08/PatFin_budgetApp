package com.patrykb.PatFin.pattern.state;

import com.patrykb.PatFin.model.Transaction;

// state 1/3
public class PlanningState implements BudgetState {
    @Override
    public String handle(Transaction transaction) {
        return "PLANNING: transaction queued -> " + transaction.getDescription();
    }

    @Override
    public BudgetState next() {
        return new ActiveState();
    }

    @Override
    public String name() {
        return "PLANNING";
    }
}
