package com.patrykb.PatFin.pattern.visitor;

public interface MetricElement {
    void accept(MetricsVisitor visitor);
}
