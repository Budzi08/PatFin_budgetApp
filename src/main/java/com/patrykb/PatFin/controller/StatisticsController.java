package com.patrykb.PatFin.controller;

import com.patrykb.PatFin.dto.StatisticsDto;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.service.StatisticsService;
import com.patrykb.PatFin.service.TransactionService;
import com.patrykb.PatFin.service.UserService;
import com.patrykb.PatFin.model.enums.TransactionType;
import com.patrykb.PatFin.config.CurrencyFormatter;
import com.patrykb.PatFin.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private UserService userService;

    @GetMapping("/summary")
    public Map<String, BigDecimal> getSummary(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        LocalDate start = null;
        LocalDate end = LocalDate.now();
        
        if (period != null && !period.isEmpty()) {
            switch (period) {
                case "1month":
                    start = LocalDate.now().minusMonths(1);
                    break;
                case "3months":
                    start = LocalDate.now().minusMonths(3);
                    break;
                case "6months":
                    start = LocalDate.now().minusMonths(6);
                    break;
                case "1year":
                    start = LocalDate.now().minusYears(1);
                    break;
            }
        } else if (startDate != null && endDate != null) {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        }

        List<Transaction> transactions;
        if (start != null) {
            transactions = transactionService.findAllByUserWithFilters(user, start, end, null, null, null, null);
        } else {
            transactions = transactionService.findAllByUser(user);
        }

        BigDecimal income = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("income", income);
        summary.put("expense", expense);
        summary.put("balance", income.subtract(expense));
        summary.put("totalTransactions", new BigDecimal(transactions.size()));

        return summary;
    }

    @GetMapping("/summary-formatted")
    public Map<String, String> getSummaryFormatted() {
        User user = getCurrentUser();
        List<Transaction> transactions = transactionService.findAllByUser(user);

        BigDecimal income = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CurrencyFormatter formatter = CurrencyFormatter.getInstance();
        AppConfig config = AppConfig.getInstance();

        Map<String, String> formatted = new HashMap<>();
        formatted.put("income", formatter.formatWithSign(income, true));
        formatted.put("expense", formatter.formatWithSign(expense, false));
        formatted.put("balance", formatter.format(income.subtract(expense)));
        formatted.put("currency", config.getDefaultCurrency());
        formatted.put("vatRate", String.valueOf(config.getVatRate()));

        return formatted;
    }

    @GetMapping("/summary-all")
    public Map<String, BigDecimal> getSummaryAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        List<Transaction> transactions = transactionService.findAllByUser(user);

        BigDecimal income = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> summary = new HashMap<>();
        summary.put("income", income);
        summary.put("expense", expense);
        summary.put("balance", income.subtract(expense));
        summary.put("totalTransactions", new BigDecimal(transactions.size()));

        return summary;
    }

    @GetMapping("/monthly")
    public Map<String, Map<String, BigDecimal>> getMonthlySummary(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
            
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        LocalDate start = null;
        LocalDate end = LocalDate.now();
        
        if (period != null && !period.isEmpty()) {
            switch (period) {
                case "1month":
                    start = LocalDate.now().minusMonths(1);
                    break;
                case "3months":
                    start = LocalDate.now().minusMonths(3);
                    break;
                case "6months":
                    start = LocalDate.now().minusMonths(6);
                    break;
                case "1year":
                    start = LocalDate.now().minusYears(1);
                    break;
            }
        } else if (startDate != null && endDate != null) {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        }

        List<Transaction> transactions;
        if (start != null) {
            transactions = transactionService.findAllByUserWithFilters(user, start, end, null, null, null, null);
        } else {
            transactions = transactionService.findAllByUser(user);
        }

        Map<String, Map<String, BigDecimal>> result = new HashMap<>();

        transactions.forEach(t -> {
            String month = t.getDate().getYear() + "-" + String.format("%02d", t.getDate().getMonthValue());
            result.putIfAbsent(month, new HashMap<>());
            Map<String, BigDecimal> map = result.get(month);

            String type = t.getType().toString().toLowerCase();
            BigDecimal amount = t.getAmount();
            
            map.put(type, map.getOrDefault(type, BigDecimal.ZERO).add(amount));
        });

        return result;
    }

    @GetMapping("/by-category")
    public Map<String, Map<String, BigDecimal>> getByCategory(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
            
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        LocalDate start = null;
        LocalDate end = LocalDate.now();
        
        if (period != null && !period.isEmpty()) {
            switch (period) {
                case "1month":
                    start = LocalDate.now().minusMonths(1);
                    break;
                case "3months":
                    start = LocalDate.now().minusMonths(3);
                    break;
                case "6months":
                    start = LocalDate.now().minusMonths(6);
                    break;
                case "1year":
                    start = LocalDate.now().minusYears(1);
                    break;
            }
        } else if (startDate != null && endDate != null) {
            start = LocalDate.parse(startDate);
            end = LocalDate.parse(endDate);
        }

        List<Transaction> transactions;
        if (start != null) {
            transactions = transactionService.findAllByUserWithFilters(user, start, end, null, null, null, null);
        } else {
            transactions = transactionService.findAllByUser(user);
        }

        Map<String, Map<String, BigDecimal>> result = new HashMap<>();

        transactions.forEach(t -> {
            String category = (t.getCategory() != null) ? t.getCategory().getName() : "Brak kategorii";
            result.putIfAbsent(category, new HashMap<>());
            Map<String, BigDecimal> map = result.get(category);

            String type = t.getType().toString().toLowerCase();
            map.put(type, map.getOrDefault(type, BigDecimal.ZERO).add(t.getAmount()));
        });

        return result;
    }

    @GetMapping("/by-category-all")
    public Map<String, Map<String, BigDecimal>> getByCategoryAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        List<Transaction> transactions = transactionService.findAllByUser(user);

        Map<String, Map<String, BigDecimal>> result = new HashMap<>();

        transactions.forEach(t -> {
            String category = (t.getCategory() != null) ? t.getCategory().getName() : "Brak kategorii";
            result.putIfAbsent(category, new HashMap<>());
            Map<String, BigDecimal> map = result.get(category);

            String type = t.getType().toString().toLowerCase();
            map.put(type, map.getOrDefault(type, BigDecimal.ZERO).add(t.getAmount()));
        });

        return result;
    }


    @GetMapping("/overview")
    public StatisticsDto.OverallStats getOverallStats() {
        User user = getCurrentUser();
        return statisticsService.getOverallStats(user);
    }

    @GetMapping("/categories/{type}")
    public List<StatisticsDto.CategoryStats> getCategoryStats(@PathVariable String type) {
        User user = getCurrentUser();
        TransactionType transactionType = TransactionType.valueOf(type.toUpperCase());
        return statisticsService.getCategoryStats(user, transactionType);
    }

    @GetMapping("/monthly-detailed")
    public List<StatisticsDto.MonthlyStats> getDetailedMonthlyStats() {
        User user = getCurrentUser();
        return statisticsService.getMonthlyStats(user);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        return userService.findByEmail(email);
    }

}
