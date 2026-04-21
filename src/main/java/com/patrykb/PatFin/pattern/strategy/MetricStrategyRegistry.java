package com.patrykb.PatFin.pattern.strategy;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class MetricStrategyRegistry {

    /**
     * "Dane sterujące" – konfiguracja dostępnych metryk.
     * Dodanie nowej metryki = dodanie wpisu tutaj
     * */
    private static final Map<String, MetricStrategy> METRIC_CONFIG = new LinkedHashMap<>();

    static {
        // Oryginalne strategie z repozytorium – rejestrowane jako dane
        METRIC_CONFIG.put("netBalance",           new NetBalanceStrategy());
        METRIC_CONFIG.put("savingsRatePercent",   new SavingsRateStrategy());
        METRIC_CONFIG.put("expenseRatioPercent",  new ExpenseRatioStrategy());
    }

    /**
     * Oblicza wybraną metrykę.
     * Metoda ta jest zamknięta
     */
    public static BigDecimal calculate(String metricName, BigDecimal income, BigDecimal expense) {
        MetricStrategy strategy = METRIC_CONFIG.get(metricName);
        if (strategy == null) {
            throw new IllegalArgumentException("Nieznana metryka: " + metricName);
        }
        return strategy.calculate(income, expense);
    }

    /**
     * Zwraca wszystkie dostępne nazwy metryk.
     * Sterowane danymi – lista pochodzi z METRIC_CONFIG
     */
    public static Iterable<String> availableMetrics() {
        return METRIC_CONFIG.keySet();
    }
}
