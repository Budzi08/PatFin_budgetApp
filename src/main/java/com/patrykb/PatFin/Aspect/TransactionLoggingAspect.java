package com.patrykb.PatFin.aspect;

import com.patrykb.PatFin.config.AuditLogger;
import com.patrykb.PatFin.dto.TransactionDto;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
/* Typ rady: @Around (przed + po + obsługa wyjątku)
 * Pointcut: execution() na konkretnych sygnaturach metod serwisu
*/
@Aspect
@Component
public class TransactionLoggingAspect {

    /**
     * Pointcut #1 – save(TransactionDto, User):
     * celuje w metodę zapisu nowej transakcji.
     */
    @Pointcut("execution(* com.patrykb.PatFin.service.TransactionService.save(..))")
    public void saveTx() {}

    /**
     * Pointcut #2 – deleteById(Long, User):
     * celuje w metodę usuwania transakcji.
     */
    @Pointcut("execution(* com.patrykb.PatFin.service.TransactionService.deleteById(..))")
    public void deleteTx() {}

    /**
     * Around – uruchamia się zamiast metody; jawnie wywołuje proceed()
     * Obejmuje oba pointcuty (save || delete)
     */
    @Around("saveTx() || deleteTx()")
    public Object logTransactionOperation(ProceedingJoinPoint pjp) throws Throwable {

        String methodName = pjp.getSignature().getName();
        Object[] args     = pjp.getArgs();

        // Wyciągamy e-mail użytkownika z argumentów wywołania (ostatni arg to User)
        String userEmail = extractEmail(args);

        System.out.printf(
                "[ASPEKT-1][BEFORE] Metoda: %s | Użytkownik: %s | Argumenty: %d%n",
                methodName, userEmail, args.length
        );

        long start = System.nanoTime();
        Object result;

        try {
            //  tu wykonuje się oryginalna metoda serwisu
            result = pjp.proceed();

        } catch (Throwable ex) {
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            System.out.printf(
                    "[ASPEKT-1][ERROR] Metoda: %s | Błąd: %s | Czas: %d ms%n",
                    methodName, ex.getMessage(), elapsed
            );
            // Zapisujemy błąd do AuditLogger
            AuditLogger.INSTANCE.log(
                    "TRANSACTION",
                    userEmail,
                    String.format("BLAD w %s: %s", methodName, ex.getMessage())
            );
            throw ex;
        }

        long elapsed = (System.nanoTime() - start) / 1_000_000;

        // Logujemy wynik do AuditLogger
        String details = buildResultDetails(methodName, result, elapsed);
        AuditLogger.INSTANCE.log("TRANSACTION", userEmail, details);

        System.out.printf(
                "[ASPEKT-1][AFTER]  Metoda: %s | Wynik: %s | Czas: %d ms%n",
                methodName, result != null ? result.getClass().getSimpleName() : "void", elapsed
        );

        return result;
    }

    // funkcje pomocnicze

    private String extractEmail(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof User u) return u.getEmail();
        }
        return "nieznany";
    }

    private String buildResultDetails(String method, Object result, long ms) {
        if (result instanceof Transaction t) {
            return String.format(
                    "Metoda: %s | Transakcja ID=%d | Kwota=%s | Czas=%d ms",
                    method, t.getId(), t.getAmount(), ms
            );
        }
        return String.format("Metoda: %s | Czas=%d ms", method, ms);
    }
}
