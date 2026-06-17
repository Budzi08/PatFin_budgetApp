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
     */

// Tydzień 9 STARY KOD, dostosuj 3 funkcje tak by były tylko na jednym poziomie abstrakcji, kolejno wywoływane funkcje coraz bardziej szczegółowe (top to botom)
//    public List<StatisticsDto.MonthlyStats> calculate(List<Object[]> rawResults) {
//        Map<String, StatisticsDto.MonthlyStats> monthlyMap = new HashMap<>();
//
//        for (Object[] result : rawResults) {
//            Integer year   = (Integer) result[0];
//            Integer month  = (Integer) result[1];
//            TransactionType type   = (TransactionType) result[2];
//            BigDecimal amount      = (BigDecimal) result[3];
//
//            String key = year + "-" + month;
//            StatisticsDto.MonthlyStats monthlyStats = monthlyMap.getOrDefault(key,
//                    new StatisticsDto.MonthlyStats(year, month, BigDecimal.ZERO, BigDecimal.ZERO));
//
//            // WZORZEC: Flyweight – pobieramy schemat matematyczny dla typu (z oryginalnego kodu)
//            TransactionTypeSchema schema = TransactionTypeSchemaFactory.getSchema(type);
//
//            if (type == TransactionType.INCOME) {
//                monthlyStats.setTotalIncome(amount);
//            } else {
//                monthlyStats.setTotalExpenses(amount);
//            }
//
//            BigDecimal impact = amount.multiply(schema.multiplier());
//            monthlyStats.setBalance(monthlyStats.getBalance().add(impact));
//
//            monthlyMap.put(key, monthlyStats);
//        }
//
//        return new ArrayList<>(monthlyMap.values());
//    }

    // Tydzień 9 ZAD4.2 dostosuj 3 funkcje tak by były tylko na jednym poziomie abstrakcji (zastosowano Step-down Rule)
    public List<StatisticsDto.MonthlyStats> calculate(List<Object[]> rawResults) {
        Map<String, StatisticsDto.MonthlyStats> monthlyMap = new HashMap<>();
        processAllResults(rawResults, monthlyMap);
        return extractValuesAsList(monthlyMap);
    }

    private void processAllResults(List<Object[]> rawResults, Map<String, StatisticsDto.MonthlyStats> monthlyMap) {
        for (Object[] result : rawResults) {
            processSingleResultRow(result, monthlyMap);
        }
    }

    private void processSingleResultRow(Object[] result, Map<String, StatisticsDto.MonthlyStats> monthlyMap) {
        Integer year = (Integer) result[0];
        Integer month = (Integer) result[1];
        TransactionType type = (TransactionType) result[2];
        BigDecimal amount = (BigDecimal) result[3];

        String key = generateKey(year, month);
        StatisticsDto.MonthlyStats stats = getOrCreateStats(monthlyMap, key, year, month);

        applyTransactionImpact(stats, type, amount);
        monthlyMap.put(key, stats);
    }

    private String generateKey(Integer year, Integer month) {
        return year + "-" + month;
    }

    private StatisticsDto.MonthlyStats getOrCreateStats(Map<String, StatisticsDto.MonthlyStats> map, String key, Integer year, Integer month) {
        return map.getOrDefault(key, new StatisticsDto.MonthlyStats(year, month, BigDecimal.ZERO, BigDecimal.ZERO));
    }

    private void applyTransactionImpact(StatisticsDto.MonthlyStats stats, TransactionType type, BigDecimal amount) {
        updateTotals(stats, type, amount);
        updateBalance(stats, type, amount);
    }

    private void updateTotals(StatisticsDto.MonthlyStats stats, TransactionType type, BigDecimal amount) {
        if (type == TransactionType.INCOME) {
            stats.setTotalIncome(amount);
        } else {
            stats.setTotalExpenses(amount);
        }
    }

    private void updateBalance(StatisticsDto.MonthlyStats stats, TransactionType type, BigDecimal amount) {
        TransactionTypeSchema schema = TransactionTypeSchemaFactory.getSchema(type);
        BigDecimal impact = amount.multiply(schema.multiplier());
        stats.setBalance(stats.getBalance().add(impact));
    }

    private List<StatisticsDto.MonthlyStats> extractValuesAsList(Map<String, StatisticsDto.MonthlyStats> map) {
        return new ArrayList<>(map.values());
    }
}
