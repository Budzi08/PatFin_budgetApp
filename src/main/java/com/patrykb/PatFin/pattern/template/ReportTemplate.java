package com.patrykb.PatFin.pattern.template;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;

import java.util.List;

public abstract class ReportTemplate {

    /**
     * OCP: algorytm zamknięty
     * Nowe sekcje dodaje się przez hook methods w podklasach.
     */
    public final String generate(User user, List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder();
        sb.append(header(user));

        // OCP: hook
        String pre = preBody(transactions);
        if (!pre.isEmpty()) {
            sb.append(" | ").append(pre);
        }

        sb.append(" | ").append(body(transactions));

        // OCP: hook
        String post = postBody(transactions);
        if (!post.isEmpty()) {
            sb.append(" | ").append(post);
        }

        sb.append(" | ").append(footer(transactions));
        return sb.toString();
    }

    protected String header(User user) {
        return "user=" + user.getEmail();
    }

    protected abstract String body(List<Transaction> transactions);

    /**
     * OCP: hook method
     */
    protected String preBody(List<Transaction> transactions) {
        return "";
    }

    /**
     * OCP: hook method
     */
    protected String postBody(List<Transaction> transactions) {
        return "";
    }

    protected String footer(List<Transaction> transactions) {
        return "count=" + transactions.size();
    }
}

