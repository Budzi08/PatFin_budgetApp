package com.patrykb.PatFin.pattern.observer;

import com.patrykb.PatFin.model.Transaction;
import java.math.BigDecimal;
import java.util.List;

// observer 2/3
public class ThresholdObserver implements TransactionObserver {
    private final BigDecimal threshold;

    public ThresholdObserver(BigDecimal threshold) {
        this.threshold = threshold;
    }

    @Override
    public void onTransaction(Transaction transaction, List<String> sink) {
        if (transaction.getAmount() != null && transaction.getAmount().compareTo(threshold) > 0) {
            sink.add("THRESHOLD: amount > " + threshold);
        } else {
            sink.add("THRESHOLD: ok");
        }
    }
}
