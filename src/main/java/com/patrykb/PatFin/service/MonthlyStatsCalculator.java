package com.patrykb.PatFin.service;

import com.patrykb.PatFin.dto.StatisticsDto;
import com.patrykb.PatFin.model.enums.TransactionType;
import com.patrykb.PatFin.pattern.flyweight.TransactionTypeSchema;
import com.patrykb.PatFin.pattern.flyweight.TransactionTypeSchemaFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class MonthlyStatsCalculator {

    /**
     * Przetwarza surowe wiersze z repozytorium (rok, miesiąc, typ, kwota)
     * na listę StatisticsDto.MonthlyStats.
     * Zachowuje oryginalne użycie wzorca Flyweight (TransactionTypeSchemaFactory).
     */
    public List<StatisticsDto.MonthlyStats> calculate(List<Object[]> rawResults) {
        Map<String, StatisticsDto.MonthlyStats> monthlyMap = new HashMap<>();

        for (Object[] result : rawResults) {
            Integer year   = (Integer) result[0];
            Integer month  = (Integer) result[1];
            TransactionType type   = (TransactionType) result[2];
            BigDecimal amount      = (BigDecimal) result[3];

            String key = year + "-" + month;
            StatisticsDto.MonthlyStats monthlyStats = monthlyMap.getOrDefault(key,
                    new StatisticsDto.MonthlyStats(year, month, BigDecimal.ZERO, BigDecimal.ZERO));

            // WZORZEC: Flyweight – pobieramy schemat matematyczny dla typu (z oryginalnego kodu)
            TransactionTypeSchema schema = TransactionTypeSchemaFactory.getSchema(type);

            if (type == TransactionType.INCOME) {
                monthlyStats.setTotalIncome(amount);
            } else {
                monthlyStats.setTotalExpenses(amount);
            }

            BigDecimal impact = amount.multiply(schema.multiplier());
            monthlyStats.setBalance(monthlyStats.getBalance().add(impact));

            monthlyMap.put(key, monthlyStats);
        }

        return new ArrayList<>(monthlyMap.values());
    }
}
