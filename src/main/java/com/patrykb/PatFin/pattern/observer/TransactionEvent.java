package com.patrykb.PatFin.pattern.observer;

import com.patrykb.PatFin.model.Transaction;


public interface TransactionEvent {
    /** Zwraca transakcję, której dotyczy zdarzenie. */
    Transaction getTransaction();

    /** Zwraca czytelną nazwę zdarzenia (np. "CREATED", "DELETED"). */
    String getEventType();
}
