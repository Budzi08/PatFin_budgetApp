package com.patrykb.PatFin.pattern.state;

import com.patrykb.PatFin.model.Transaction;

// state 3/3
public class ClosedState implements BudgetState {
    @Override
    public String handle(Transaction transaction) {
        return "CLOSED: transaction rejected -> " + transaction.getDescription();
    }

    @Override
    public BudgetState next() {
        return this;
    }

    @Override
    public String name() {
        return "CLOSED";
    }
}
