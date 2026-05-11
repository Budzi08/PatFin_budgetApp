package com.patrykb.PatFin.pattern.observer;

import com.patrykb.PatFin.model.Transaction;

public class TransactionCreatedEvent implements TransactionEvent {

    private final Transaction transaction;

    public TransactionCreatedEvent(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public String getEventType() {
        return "CREATED";
    }
}
