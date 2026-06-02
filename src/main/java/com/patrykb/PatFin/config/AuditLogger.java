package com.patrykb.PatFin.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum AuditLogger {

    INSTANCE;

    private final List<String> logs = Collections.synchronizedList(new ArrayList<>());

    // SRP: formatowanie wpisu delegowane do dedykowanej klasy
    private final AuditEntryFormatter formatter = new AuditEntryFormatter();

    // Tydzień 9 STARY KOD, użycie synonimów action/operation oraz nazwy userEmail niezgodnej z resztą programu (email)
    // public void log(String action, String userEmail, String details) {
    //     // SRP: AuditLogger tylko przechowuje gotowy wpis – nie formatuje go sam
    //     String entry = formatter.format(action, userEmail, details);
    //     logs.add(entry);
    //     System.out.println("AUDIT: " + entry);
    // }
    //
    // public void logTransaction(String userEmail, String operation, Long transactionId) {
    //     log("TRANSACTION", userEmail,
    //             String.format("Operacja: %s, ID transakcji: %d", operation, transactionId));
    // }
    //
    // public void logAuth(String userEmail, String operation) {
    //     log("AUTH", userEmail, "Operacja: " + operation);
    // }
    //
    // public void logAdmin(String userEmail, String operation, String details) {
    //     log("ADMIN", userEmail, String.format("Operacja: %s | %s", operation, details));
    // }

    // Tydzień 9 ZAD1.1 Ujednolicenie nazw i usunięcie synonimów (zastąpienie action/operation przez actionType oraz userEmail przez email)
    public void log(String actionType, String email, String details) {
        // SRP: AuditLogger tylko przechowuje gotowy wpis – nie formatuje go sam
        String entry = formatter.format(actionType, email, details);
        logs.add(entry);
        System.out.println("AUDIT: " + entry);
    }

    public void logTransaction(String email, String actionType, Long transactionId) {
        log("TRANSACTION", email,
                String.format("Operacja: %s, ID transakcji: %d", actionType, transactionId));
    }

    public void logAuth(String email, String actionType) {
        log("AUTH", email, "Operacja: " + actionType);
    }

    public void logAdmin(String email, String actionType, String details) {
        log("ADMIN", email, String.format("Operacja: %s | %s", actionType, details));
    }

    public List<String> getLogs() {
        return Collections.unmodifiableList(logs);
    }

    public List<String> getLastLogs(int count) {
        int size = logs.size();
        int from = Math.max(0, size - count);
        return Collections.unmodifiableList(logs.subList(from, size));
    }
}