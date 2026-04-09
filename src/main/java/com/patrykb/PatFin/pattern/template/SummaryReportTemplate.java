package com.patrykb.PatFin.pattern.template;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;

// template 1/3
public class SummaryReportTemplate extends ReportTemplate {
    @Override
    protected String body(List<Transaction> transactions) {
        BigDecimal income = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal expense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return "summary[income=" + income + ",expense=" + expense + "]";
    }
}
