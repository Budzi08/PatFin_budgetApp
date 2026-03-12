package com.patrykb.PatFin.pattern.bridge;

public class PieChartRenderer extends ChartRenderer {
    public PieChartRenderer(ChartTheme theme) {
        super(theme);
    }

    @Override
    public String renderChart(String dataPoints) {
        return theme.applyTheme("Rendering Pie Chart with data: " + dataPoints);
    }
}