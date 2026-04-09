package com.patrykb.PatFin.pattern.observer;

import com.patrykb.PatFin.model.Transaction;
import java.util.ArrayList;
import java.util.List;

public class TransactionEventPublisher {
    private final List<TransactionObserver> observers = new ArrayList<>();

    public void subscribe(TransactionObserver observer) {
        observers.add(observer);
    }

    public void publish(Transaction transaction, List<String> sink) {
        for (TransactionObserver observer : observers) {
            observer.onTransaction(transaction, sink);
        }
    }
}
