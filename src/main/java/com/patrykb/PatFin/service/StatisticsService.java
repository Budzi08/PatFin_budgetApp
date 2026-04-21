package com.patrykb.PatFin.service;

import com.patrykb.PatFin.dto.StatisticsDto;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.enums.TransactionType;
import com.patrykb.PatFin.pattern.iterator.PatFinIterator;
import com.patrykb.PatFin.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    // SRP: obliczenia miesięczne delegowane do dedykowanej klasy
    @Autowired
    private MonthlyStatsCalculator monthlyStatsCalculator;

    static class StatsDtoFactory {
        static StatisticsDto.CategoryStats createCategoryStats(String name, BigDecimal amount, Long count) {
            return new StatisticsDto.CategoryStats(name, amount, count);
        }
    }

    public StatisticsDto.OverallStats getOverallStats(User user) {
        BigDecimal totalIncome   = transactionRepository.findTotalAmountByUserAndType(user, TransactionType.INCOME);
        BigDecimal totalExpenses = transactionRepository.findTotalAmountByUserAndType(user, TransactionType.EXPENSE);

        return StatisticsDto.OverallStats.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .totalTransactions(transactionRepository.countByUser(user))
                .expensesByCategory(getCategoryStats(user, TransactionType.EXPENSE))
                .incomeByCategory(getCategoryStats(user, TransactionType.INCOME))
                .monthlyStats(getMonthlyStats(user))
                .build();
    }

    public List<StatisticsDto.CategoryStats> getCategoryStats(User user, TransactionType type) {
        List<Object[]> results = transactionRepository.findAmountByCategoryAndType(user, type);
        List<StatisticsDto.CategoryStats> categoryStats = new ArrayList<>();

        // L5 Iterator #3 (zachowany z oryginalnego kodu)
        PatFinIterator<Object[]> resIt = new PatFinIterator<>() {
            private int i = 0;
            public boolean hasNext() { return i < results.size(); }
            public Object[] next()   { return results.get(i++); }
        };

        while (resIt.hasNext()) {
            Object[] res = resIt.next();
            categoryStats.add(StatsDtoFactory.createCategoryStats(
                    (String) res[0], (BigDecimal) res[1], (Long) res[2]));
        }

        return categoryStats;
    }

    public List<StatisticsDto.MonthlyStats> getMonthlyStats(User user) {
        List<Object[]> results = transactionRepository.findMonthlyStatsByUser(user);
        // SRP: delegujemy przeliczenie do MonthlyStatsCalculator
        return monthlyStatsCalculator.calculate(results);
    }
}

