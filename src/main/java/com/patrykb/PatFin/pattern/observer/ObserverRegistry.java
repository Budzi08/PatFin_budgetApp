package com.patrykb.PatFin.pattern.observer;

import com.patrykb.PatFin.model.Transaction;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ObserverRegistry {

    /**
     * "Dane sterujące" – konfiguracja aktywnych obserwatorów.
     */
    private static final Map<String, TransactionObserver> OBSERVER_CONFIG = new LinkedHashMap<>();

    static {
        // Oryginalne obserwatory z repozytorium – rejestrowane jako dane
        OBSERVER_CONFIG.put("audit",     new AuditObserver());
        OBSERVER_CONFIG.put("threshold", new ThresholdObserver(new BigDecimal("1000")));
        // Rozszerzenie to nowy wpis
    }

    /**
     * Powiadamia wszystkich zarejestrowanych obserwatorów.
     */
    public static void notifyAll(Transaction transaction, List<String> sink) {
        for (TransactionObserver observer : OBSERVER_CONFIG.values()) {
            observer.onTransaction(transaction, sink);
        }
    }
}
