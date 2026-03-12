package com.patrykb.PatFin.pattern.bridge;

public abstract class Report {
    protected ReportFormatter formatter;

    protected Report(ReportFormatter formatter) {
        this.formatter = formatter;
    }

    public abstract String generate();
}