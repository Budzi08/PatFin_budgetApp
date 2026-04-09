package com.patrykb.PatFin.pattern.template;

import com.patrykb.PatFin.model.Transaction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// template 2/3
public class CategoryReportTemplate extends ReportTemplate {
    @Override
    protected String body(List<Transaction> transactions) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Transaction transaction : transactions) {
            String name = transaction.getCategory() == null ? "NO_CATEGORY" : transaction.getCategory().getName();
            counts.put(name, counts.getOrDefault(name, 0) + 1);
        }
        return "category=" + counts;
    }
}
