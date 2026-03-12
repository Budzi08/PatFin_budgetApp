package com.patrykb.PatFin.pattern.bridge;

public class FinancialReport extends Report {
    private final String summary;

    public FinancialReport(ReportFormatter formatter, String summary) {
        super(formatter);
        this.summary = summary;
    }

    @Override
    public String generate() {
        return formatter.format("Financial Summary", summary);
    }
}