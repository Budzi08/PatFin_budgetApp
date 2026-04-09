package com.patrykb.PatFin.pattern.observer;

import com.patrykb.PatFin.model.Transaction;
import java.util.List;

// observer 3/3
public class CategoryObserver implements TransactionObserver {
    @Override
    public void onTransaction(Transaction transaction, List<String> sink) {
        String category = transaction.getCategory() == null ? "NO_CATEGORY" : transaction.getCategory().getName();
        sink.add("CATEGORY: " + category);
    }
}
