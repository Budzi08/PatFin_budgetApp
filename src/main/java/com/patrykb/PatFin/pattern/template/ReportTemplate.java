package com.patrykb.PatFin.pattern.template;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;

import java.util.List;

public abstract class ReportTemplate {

    public final String generate(User user, List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder();
        sb.append(header(user));
        sb.append(" | ");
        sb.append(body(transactions));
        sb.append(" | ");
        sb.append(footer(transactions));
        return sb.toString();
    }

    protected String header(User user) {
        return "user=" + user.getEmail();
    }

    protected abstract String body(List<Transaction> transactions);

    protected String footer(List<Transaction> transactions) {
        return "count=" + transactions.size();
    }
}
