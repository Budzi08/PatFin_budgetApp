package com.patrykb.PatFin.tydzien8.zad2;
import com.patrykb.PatFin.model.Transaction;

public interface TransactionNotifier {
    void notifyUser(Transaction transaction);

    boolean isApplicable(Transaction savedTransaction);
}
