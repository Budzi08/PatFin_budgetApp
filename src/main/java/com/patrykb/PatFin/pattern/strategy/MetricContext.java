package com.patrykb.PatFin.pattern.strategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class MetricContext {

    // OCP: rejestr strategii – nowa strategia = nowy wpis w mapie, bez zmiany tej klasy
    private final Map<String, MetricStrategy> registry = new HashMap<>();

    public MetricContext() {
        // Rejestrujemy oryginalne strategie z repozytorium
        register(new NetBalanceStrategy());
        register(new SavingsRateStrategy());
        register(new ExpenseRatioStrategy());
    }

    /**
     * OCP: punkt rozszerzenia – dodanie nowej metryki bez modyfikacji klasy.
     */
    public void register(MetricStrategy strategy) {
        registry.put(strategy.name(), strategy);
    }

    /**
     * Oblicza wybraną metrykę po nazwie.
     */
    public BigDecimal calculate(String metricName, BigDecimal income, BigDecimal expense) {
        MetricStrategy strategy = registry.get(metricName);
        if (strategy == null) {
            throw new IllegalArgumentException("Nieznana metryka: " + metricName);
        }
        return strategy.calculate(income, expense);
    }

    private MetricStrategy strategy;

    public void setStrategy(MetricStrategy strategy) {
        this.strategy = strategy;
    }

    public BigDecimal calculate(BigDecimal income, BigDecimal expense) {
        return strategy.calculate(income, expense);
    }

    public String getStrategyName() {
        return strategy.name();
    }
}

