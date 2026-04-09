package com.patrykb.PatFin.pattern.observer;

import com.patrykb.PatFin.model.Transaction;
import java.util.List;

// observer 1/3
public class AuditObserver implements TransactionObserver {
    @Override
    public void onTransaction(Transaction transaction, List<String> sink) {
        sink.add("AUDIT: " + transaction.getDescription());
    }
}
