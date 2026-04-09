package com.patrykb.PatFin.pattern.observer;

import com.patrykb.PatFin.model.Transaction;
import java.util.List;

public interface TransactionObserver {
    void onTransaction(Transaction transaction, List<String> sink);
}
