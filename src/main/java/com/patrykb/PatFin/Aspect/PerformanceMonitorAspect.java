package com.patrykb.PatFin.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Typ rady: @Around- może zarówno mierzyć czas
 *  przed jak i po wykonaniu metody w jednym bloku kodu
 * Pointcut: @within() na adnotacji @Service + osobny execution() dla StatisticsService (wszystkie @Service)
 */
@Aspect
@Component
public class PerformanceMonitorAspect {

    // Próg ostrzeżenia
    private static final long SLOW_THRESHOLD_MS = 200L;

    // Próg ostrzeżenia dla StatisticsService (zapytania agregujące).
    private static final long STATS_THRESHOLD_MS = 100L;

    private final Map<String, long[]> callStats = new ConcurrentHashMap<>();

    /**
     * Pointcut #1 – @within obejmuje automatycznie każdy nowy serwis bez zmiany aspektu.
     */
    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void anyServiceMethod() {}

    /**
     * Pointcut #2, Osobny pointcut umożliwia zastosowanie innego progu ostrzeżeń.
     */
    @Pointcut("execution(* com.patrykb.PatFin.service.StatisticsService.*(..))")
    public void statisticsMethod() {}

    /**
     * Rada Around dla wszystkich @Service z wyjątkiem StatisticsService.
     */
    @Around("anyServiceMethod() && !statisticsMethod()")
    public Object monitorServicePerformance(ProceedingJoinPoint pjp) throws Throwable {
        return measure(pjp, SLOW_THRESHOLD_MS);
    }

    /**
     * Rada Around specjalnie dla StatisticsService.
     */
    @Around("statisticsMethod()")
    public Object monitorStatisticsPerformance(ProceedingJoinPoint pjp) throws Throwable {
        return measure(pjp, STATS_THRESHOLD_MS);
    }

    // pomiar

    private Object measure(ProceedingJoinPoint pjp, long threshold) throws Throwable {
        String className  = pjp.getTarget().getClass().getSimpleName();
        String methodName = pjp.getSignature().getName();
        String key        = className + "." + methodName;

        long start = System.nanoTime();
        Object result;

        try {
            // wykonanie oryginalnej metody serwisu
            result = pjp.proceed();
        } finally {
            long elapsed = (System.nanoTime() - start) / 1_000_000;
            recordStats(key, elapsed);

            System.out.printf(
                    "[ASPEKT-3][PERF] %s | Czas: %d ms%s%n",
                    key,
                    elapsed,
                    elapsed > threshold ? " <<< WOLNA OPERACJA (próg: " + threshold + " ms)" : ""
            );

            if (elapsed > threshold) {
                emitSlowOperationWarning(key, elapsed, threshold);
            }
        }

        return result;
    }


     // Zbiera statystyki w ConcurrentHashMap:
    private void recordStats(String key, long elapsedMs) {
        callStats.compute(key, (k, existing) -> {
            if (existing == null) return new long[]{1, elapsedMs};
            existing[0]++;
            existing[1] += elapsedMs;
            return existing;
        });
    }

    private void emitSlowOperationWarning(String key, long elapsed, long threshold) {
        long[] stats = callStats.get(key);
        long callCount = stats != null ? stats[0] : 0;
        long avgMs     = (stats != null && stats[0] > 0) ? stats[1] / stats[0] : elapsed;

        System.out.printf(
                "[ASPEKT-3][WARN]  Wolna operacja: %s | Czas=%d ms | Prog=%d ms | " +
                        "Łączne wywołania=%d | Śr. czas=%d ms%n",
                key, elapsed, threshold, callCount, avgMs
        );
    }


     // Zwracanie zebranych statystyk

    public Map<String, long[]> getCallStats() {
        return Map.copyOf(callStats);
    }

     // Drukuje podsumowanie wszystkich zebranych statystyk.
    public void printSummary() {
        System.out.println("[ASPEKT-3][SUMMARY] === Statystyki wydajności serwisów ===");
        callStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue()[1], a.getValue()[1]))
                .forEach(e -> {
                    long calls = e.getValue()[0];
                    long total = e.getValue()[1];
                    long avg   = calls > 0 ? total / calls : 0;
                    System.out.printf(
                            "[ASPEKT-3][SUMMARY] %-60s wywołania=%-5d łącznie=%-6d ms śr=%d ms%n",
                            e.getKey(), calls, total, avg
                    );
                });
    }
}
