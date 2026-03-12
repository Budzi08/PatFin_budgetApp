package com.patrykb.PatFin.pattern.decorator;

public class SimpleExportWriter implements ExportWriter {
    @Override
    public String write(String data) {
        return data;
    }
}