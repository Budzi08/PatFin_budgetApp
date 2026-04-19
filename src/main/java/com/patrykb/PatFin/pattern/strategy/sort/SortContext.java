package com.patrykb.PatFin.pattern.strategy.sort;

import java.util.List;

public class SortContext {
    private SortStrategy strategy;
    public SortContext() {}
    public void setStrategy(SortStrategy strategy) { this.strategy = strategy; }
    public void executeSort(List<String> items) { if(strategy != null) strategy.sort(items); }
}
