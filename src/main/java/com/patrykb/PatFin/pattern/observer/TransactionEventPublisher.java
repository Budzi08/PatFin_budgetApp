package com.patrykb.PatFin.pattern.observer;

import java.util.ArrayList;
import java.util.List;


public class TransactionEventPublisher {

    private final List<TransactionObserver> observers = new ArrayList<>();

    public void subscribe(TransactionObserver observer) {
        observers.add(observer);
    }

    /**
     * OCP: przyjmuje interfejs
     */
    public void publish(TransactionEvent event, List<String> sink) {
        for (TransactionObserver observer : observers) {
            observer.onTransaction(event.getTransaction(), sink);
        }
    }

    public void publish(com.patrykb.PatFin.model.Transaction transaction, List<String> sink) {
        publish(new TransactionCreatedEvent(transaction), sink);
    }
}

