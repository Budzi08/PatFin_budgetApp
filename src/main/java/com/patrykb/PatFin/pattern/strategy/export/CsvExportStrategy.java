package com.patrykb.PatFin.pattern.strategy.export;

public class CsvExportStrategy implements ExportStrategy {
    @Override
    public String exportData(String data) { return "data\n" + data; }
}
