package com.patrykb.PatFin.pattern.strategy.export;

public class ExportContext {
    private ExportStrategy strategy;
    public ExportContext(ExportStrategy strategy) { this.strategy = strategy; }
    public void setStrategy(ExportStrategy strategy) { this.strategy = strategy; }
    public String executeExport(String data) { return strategy.exportData(data); }
}
