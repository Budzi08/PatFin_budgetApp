package com.patrykb.PatFin.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.patrykb.PatFin.pattern.flyweight.AuditAction;
import com.patrykb.PatFin.pattern.flyweight.AuditActionFactory;

public enum AuditLogger {

    INSTANCE;

    private final List<String> logs = Collections.synchronizedList(new ArrayList<>());
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void log(String action, String userEmail, String details) {
        // WZORZEC: Flyweight - "Wymieniamy" zwykły String na współdzielony obiekt pyłka
        AuditAction actionFlyweight = AuditActionFactory.getAction(action);

        String entry = String.format("[%s] [%s] Użytkownik: %s | %s",
                LocalDateTime.now().format(formatter),actionFlyweight.severity(), actionFlyweight.type(), userEmail, details);
        logs.add(entry);
        System.out.println("AUDIT: " + entry);
    }

    public void logTransaction(String userEmail, String operation, Long transactionId) {
        log("TRANSACTION", userEmail,
                String.format("Operacja: %s, ID transakcji: %d", operation, transactionId));
    }

    public void logAuth(String userEmail, String operation) {
        log("AUTH", userEmail, "Operacja: " + operation);
    }

    public void logAdmin(String userEmail, String operation, String details) {
        log("ADMIN", userEmail, String.format("Operacja: %s | %s", operation, details));
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
