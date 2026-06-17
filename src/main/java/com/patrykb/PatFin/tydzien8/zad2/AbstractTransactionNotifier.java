package com.patrykb.PatFin.tydzien8.zad2;
import com.patrykb.PatFin.model.Transaction;

public abstract class AbstractTransactionNotifier implements TransactionNotifier {
    // Wspólna logika dla wszystkich powiadomień
    protected String buildAlertMessage(Transaction t) {
        return "UWAGA! Zarejestrowano transakcję o wartości: " + t.getAmount() + " PLN (" + t.getDescription() + ")";
    }
}