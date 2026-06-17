package com.patrykb.PatFin.aspect;

import com.patrykb.PatFin.config.AuditLogger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Typy rad: @Before-rejestruje każde wejście do endpointu
 * ,@AfterReturning- rejestruje pomyślne zakończenie z informacją o typie odpowiedzi
 * ,@AfterThrowing– wychwytuje wyjątki bezpieczeństwa i zapisuje próbę nieautoryzowanego dostępu
 * Pointcut: within() na pakiecie kontrolerów
 */
@Aspect
@Component
public class SecurityAuditAspect {

    /**
     * Pointcut obejmuje automatycznie każdy nowy kontroler dodany do pakietu.
     */
    @Pointcut("within(com.patrykb.PatFin.controller..*)")
    public void anyControllerMethod() {}

    /**
     * Rada Before
     * Odczytuje aktualnie zalogowanego użytkownika z SecurityContextHolder i loguje wejście.
     */
    @Before("anyControllerMethod()")
    public void auditControllerEntry(JoinPoint jp) {
        String user    = getCurrentUser();
        String method  = jp.getSignature().toShortString();
        int    argCount = jp.getArgs().length;

        System.out.printf(
                "[ASPEKT-2][BEFORE] Endpoint: %s | Użytkownik: %s | Liczba args: %d%n",
                method, user, argCount
        );

        // Zapisujemy każde wejście do endpointu do systemu audytu aplikacji
        AuditLogger.INSTANCE.log(
                "ADMIN",
                user,
                String.format("Wywołanie endpointu: %s", method)
        );
    }

    /**
     * Rada AfterReturning
     * Parametr 'returnValue' przechwytuje zwracaną wartość
     */
    @AfterReturning(pointcut = "anyControllerMethod()", returning = "returnValue")
    public void auditControllerSuccess(JoinPoint jp, Object returnValue) {
        String user   = getCurrentUser();
        String method = jp.getSignature().getName();
        String type   = returnValue != null
                ? returnValue.getClass().getSimpleName()
                : "void";

        System.out.printf(
                "[ASPEKT-2][AFTER-OK] Endpoint: %s | Użytkownik: %s | Typ odpowiedzi: %s%n",
                method, user, type
        );
    }

    /**
     * Rada AfterThrowing gdy metoda rzuci wyjątek.
     * Zapisujemy próbę nieautoryzowanego dostępu.
     */
    @AfterThrowing(pointcut = "anyControllerMethod()", throwing = "ex")
    public void auditControllerException(JoinPoint jp, Exception ex) {
        String user   = getCurrentUser();
        String method = jp.getSignature().toShortString();

        System.out.printf(
                "[ASPEKT-2][AFTER-ERR] Endpoint: %s | Użytkownik: %s | Wyjątek: %s%n",
                method, user, ex.getMessage()
        );

        // Próba nieautoryzowanego dostępu
        if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("unauthorized")) {
            AuditLogger.INSTANCE.logAuth(
                    user,
                    String.format("NIEAUTORYZOWANY DOSTEP do: %s", method)
            );
        } else {
            AuditLogger.INSTANCE.log(
                    "ADMIN",
                    user,
                    String.format("BLAD w endpointcie %s: %s", method, ex.getMessage())
            );
        }
    }

    // funkcje pomocnicze


    private String getCurrentUser() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof String email) {
                return email;
            }
        } catch (Exception ignored) {}
        return "anonimowy";
    }
}
