package com.patrykb.PatFin.pattern.template;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class DataDrivenReportTemplate {

    /**
     * Kontrakt sekcji raportu – każda sekcja to funkcja (User, List<Transaction>) → String.
     * Nowa sekcja = nowa implementacja tego interfejsu; DataDrivenReportTemplate nie wie
     * nic o jej istnieniu.
     */
    @FunctionalInterface
    public interface ReportSection {
        String render(User user, List<Transaction> transactions);
    }

    /**
     * "Dane sterujące" – konfiguracja sekcji i ich kolejność.
     * Dodanie nowej sekcji = dodanie wpisu tutaj (lub załadowanie z zewnątrz).
     * Kod metody generate() pozostaje NIEZMIENIONY.
     */
    private static final List<ReportSection> SECTIONS = new ArrayList<>();

    static {
        // Oryginalne sekcje z ReportTemplate – zarejestrowane jako dane
        SECTIONS.add((user, txs)  -> "user=" + user.getEmail());
        SECTIONS.add((user, txs)  -> buildBody(txs));
        SECTIONS.add((user, txs)  -> "count=" + txs.size());
    }

    /**
     * OCP: zamknięta na modyfikacje – iteruje dane z SECTIONS.
     */
    public String generate(User user, List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SECTIONS.size(); i++) {
            if (i > 0) sb.append(" | ");
            sb.append(SECTIONS.get(i).render(user, transactions));
        }
        return sb.toString();
    }

    /**
     * Domyślna logika treści raportu – odpowiednik body() z ReportTemplate.

     */
    private static String buildBody(List<Transaction> transactions) {
        if (transactions.isEmpty()) return "brak transakcji";
        return "transactions=" + transactions.size()
                + ", first=" + transactions.get(0).getDescription();
    }
}
