package com.patrykb.PatFin.pattern.strategy.export;

public class JsonExportStrategy implements ExportStrategy {
    @Override
    public String exportData(String data) { return "{ \"data\": \"" + data + "\" }"; }
}
