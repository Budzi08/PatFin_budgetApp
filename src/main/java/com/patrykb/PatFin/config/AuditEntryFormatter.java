package com.patrykb.PatFin.config;

import com.patrykb.PatFin.pattern.flyweight.AuditAction;
import com.patrykb.PatFin.pattern.flyweight.AuditActionFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditEntryFormatter {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Tworzy gotowy wpis audytowy na podstawie danych zdarzenia.
     */
    // Tydzień 9 STARY KOD, użycie nazw action (synonim type/operation) oraz userEmail (synonim email)
    // public String format(String action, String userEmail, String details) {
    //     AuditAction actionFlyweight = AuditActionFactory.getAction(action);
    //
    //     return String.format("[%s] [%s] Użytkownik: %s | %s",
    //             LocalDateTime.now().format(FORMATTER),
    //             actionFlyweight.severity(),
    //             actionFlyweight.type(),
    //             userEmail,
    //             details);
    // }

    // Tydzień 9 ZAD1.2 Zmiana nazw parametrów na jednoznaczne i spójne z resztą aplikacji (actionType oraz email)
    public String format(String actionType, String email, String details) {
        AuditAction actionFlyweight = AuditActionFactory.getAction(actionType);

        return String.format("[%s] [%s] Użytkownik: %s | %s",
                LocalDateTime.now().format(FORMATTER),
                actionFlyweight.severity(),
                actionFlyweight.type(),
                email,
                details);
    }
}