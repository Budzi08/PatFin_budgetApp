package com.patrykb.PatFin.pattern.state;

import com.patrykb.PatFin.model.Transaction;

public class BudgetLifecycleContext {
    private BudgetState state = new PlanningState();

    public String handle(Transaction transaction) {
        return state.handle(transaction);
    }

    public void nextState() {
        state = state.next();
    }

    public String currentStateName() {
        return state.name();
    }
}
