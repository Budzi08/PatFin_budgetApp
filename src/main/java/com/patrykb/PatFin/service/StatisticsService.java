package com.patrykb.PatFin.service;

import com.patrykb.PatFin.dto.StatisticsDto;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.enums.TransactionType;
import com.patrykb.PatFin.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    public StatisticsDto.OverallStats getOverallStats(User user) {
        StatisticsDto.OverallStats stats = new StatisticsDto.OverallStats();

        BigDecimal totalIncome = transactionRepository.findTotalAmountByUserAndType(user, TransactionType.INCOME);
        BigDecimal totalExpenses = transactionRepository.findTotalAmountByUserAndType(user, TransactionType.EXPENSE);
        
        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        if (totalExpenses == null) totalExpenses = BigDecimal.ZERO;

        stats.setTotalIncome(totalIncome);
        stats.setTotalExpenses(totalExpenses);
        stats.setCurrentBalance(totalIncome.subtract(totalExpenses));
        stats.setTotalTransactions(transactionRepository.countByUser(user));

        // Statystyki per kategoria
        stats.setExpensesByCategory(getCategoryStats(user, TransactionType.EXPENSE));
        stats.setIncomeByCategory(getCategoryStats(user, TransactionType.INCOME));

        // Statystyki miesięczne
        stats.setMonthlyStats(getMonthlyStats(user));

        return stats;
    }

    public List<StatisticsDto.CategoryStats> getCategoryStats(User user, TransactionType type) {
        List<Object[]> results = transactionRepository.findAmountByCategoryAndType(user, type);
        List<StatisticsDto.CategoryStats> categoryStats = new ArrayList<>();

        for (Object[] result : results) {
            String categoryName = (String) result[0];
            BigDecimal totalAmount = (BigDecimal) result[1];
            Long transactionCount = (Long) result[2];

            categoryStats.add(new StatisticsDto.CategoryStats(categoryName, totalAmount, transactionCount));
        }

        return categoryStats;
    }

    public List<StatisticsDto.MonthlyStats> getMonthlyStats(User user) {
        List<Object[]> results = transactionRepository.findMonthlyStatsByUser(user);
        Map<String, StatisticsDto.MonthlyStats> monthlyMap = new HashMap<>();

        for (Object[] result : results) {
            Integer year = (Integer) result[0];
            Integer month = (Integer) result[1];
            TransactionType type = (TransactionType) result[2];
            BigDecimal amount = (BigDecimal) result[3];

            String key = year + "-" + month;
            StatisticsDto.MonthlyStats monthlyStats = monthlyMap.getOrDefault(key, 
                new StatisticsDto.MonthlyStats(year, month, BigDecimal.ZERO, BigDecimal.ZERO));

            if (type == TransactionType.INCOME) {
                monthlyStats.setTotalIncome(amount);
            } else {
                monthlyStats.setTotalExpenses(amount);
            }

            monthlyStats.setBalance(monthlyStats.getTotalIncome().subtract(monthlyStats.getTotalExpenses()));
            monthlyMap.put(key, monthlyStats);
        }

        return new ArrayList<>(monthlyMap.values());
    }
}
