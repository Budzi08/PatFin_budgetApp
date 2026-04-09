package com.patrykb.PatFin.pattern.template;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// template 3/3
public class MonthlyReportTemplate extends ReportTemplate {
    @Override
    protected String body(List<Transaction> transactions) {
        Map<YearMonth, BigDecimal> monthly = new LinkedHashMap<>();
        for (Transaction transaction : transactions) {
            if (transaction.getDate() == null || transaction.getAmount() == null) {
                continue;
            }
            YearMonth key = YearMonth.from(transaction.getDate());
            BigDecimal signed = transaction.getType() == TransactionType.INCOME
                    ? transaction.getAmount()
                    : transaction.getAmount().negate();
            monthly.put(key, monthly.getOrDefault(key, BigDecimal.ZERO).add(signed));
        }
        return "monthlyBalance=" + monthly;
    }
}
