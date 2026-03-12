package com.patrykb.PatFin.pattern.bridge;

public abstract class ChartRenderer {
    protected ChartTheme theme;

    protected ChartRenderer(ChartTheme theme) {
        this.theme = theme;
    }

    public abstract String renderChart(String dataPoints);
}