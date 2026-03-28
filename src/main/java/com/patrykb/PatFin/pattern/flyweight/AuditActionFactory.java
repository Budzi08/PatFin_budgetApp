package com.patrykb.PatFin.pattern.flyweight;

import java.util.HashMap;
import java.util.Map;


//Fabryka pyłków - zarządza pulą obiektów AuditAction

public class AuditActionFactory {
    private static final Map<String, AuditAction> actions = new HashMap<>();

    public static AuditAction getAction(String type) {
        return actions.computeIfAbsent(type, t -> {
            // Logika klasyfikacji - wykonywana tylko raz dla każdego typu
            String severity = t.contains("DELETE") || t.contains("ADMIN") ? "HIGH" : "LOW";
            return new AuditAction(t, severity);
        });
    }
}