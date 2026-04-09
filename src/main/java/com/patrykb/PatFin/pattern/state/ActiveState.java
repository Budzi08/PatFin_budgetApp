package com.patrykb.PatFin.pattern.state;

import com.patrykb.PatFin.model.Transaction;

// state 2/3
public class ActiveState implements BudgetState {
    @Override
    public String handle(Transaction transaction) {
        return "ACTIVE: transaction accepted -> " + transaction.getDescription();
    }

    @Override
    public BudgetState next() {
        return new ClosedState();
    }

    @Override
    public String name() {
        return "ACTIVE";
    }
}
