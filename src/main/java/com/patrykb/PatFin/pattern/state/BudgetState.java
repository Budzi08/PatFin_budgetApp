package com.patrykb.PatFin.pattern.state;

import com.patrykb.PatFin.model.Transaction;

public interface BudgetState {
    String handle(Transaction transaction);

    BudgetState next();

    String name();
}
