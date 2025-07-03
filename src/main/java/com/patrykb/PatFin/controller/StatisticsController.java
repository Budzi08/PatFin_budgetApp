package com.patrykb.PatFin.controller;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.service.TransactionService;
import com.patrykb.PatFin.service.UserService;
import com.patrykb.PatFin.model.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping("/summary")
    public Map<String, BigDecimal> getSummary() {
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

        return summary;
    }

    @GetMapping("/monthly")
    public Map<String, Map<String, BigDecimal>> getMonthlySummary() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        List<Transaction> transactions = transactionService.findAllByUser(user);

        Map<String, Map<String, BigDecimal>> result = new HashMap<>();

        transactions.forEach(t -> {
            String month = t.getDate().getYear() + "-" + String.format("%02d", t.getDate().getMonthValue());
            result.putIfAbsent(month, new HashMap<>());
            Map<String, BigDecimal> map = result.get(month);

            String type = t.getType().toString().toLowerCase();
            map.put(type, map.getOrDefault(type, BigDecimal.ZERO).add(t.getAmount()));
        });

        return result;
    }

    @GetMapping("/by-category")
    public Map<String, Map<String, BigDecimal>> getByCategory() {
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

}
