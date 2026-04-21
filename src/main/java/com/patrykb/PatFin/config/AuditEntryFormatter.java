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
    public String format(String action, String userEmail, String details) {
        AuditAction actionFlyweight = AuditActionFactory.getAction(action);

        return String.format("[%s] [%s] Użytkownik: %s | %s",
                LocalDateTime.now().format(FORMATTER),
                actionFlyweight.severity(),
                actionFlyweight.type(),
                userEmail,
                details);
    }
}
