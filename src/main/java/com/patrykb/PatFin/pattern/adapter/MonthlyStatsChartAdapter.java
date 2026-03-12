package com.patrykb.PatFin.pattern.adapter;

import com.patrykb.PatFin.dto.StatisticsDto.MonthlyStats;
import java.math.BigDecimal;

public class MonthlyStatsChartAdapter implements ChartData {
    private final MonthlyStats stats;

    public MonthlyStatsChartAdapter(MonthlyStats stats) {
        this.stats = stats;
    }

    @Override
    public String getLabel() {
        return stats.getYear() + "-" + String.format("%02d", stats.getMonth());
    }

    @Override
    public BigDecimal getValue() {
        return stats.getBalance();
    }
}