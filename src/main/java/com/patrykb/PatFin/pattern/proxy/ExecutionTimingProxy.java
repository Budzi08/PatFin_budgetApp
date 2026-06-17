package com.patrykb.PatFin.pattern.proxy;

import com.patrykb.PatFin.dto.StatisticsDto;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.service.StatisticsService;
import com.patrykb.PatFin.config.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class ExecutionTimingProxy {

    // Tydzień 9 ZAD8 wprowadzono znaczącą stałą w celu wyeliminowania magicznej liczby w kodzie
    private static final long SLOW_EXECUTION_THRESHOLD_MS = 50;

    @Autowired
    private StatisticsService statisticsService;

    public StatisticsDto.OverallStats getStatsWithTiming(User user) {
        StopWatch sw = new StopWatch();
        sw.start();

        StatisticsDto.OverallStats stats = statisticsService.getOverallStats(user);

        sw.stop();
        long time = sw.getTotalTimeMillis();

        // Tydzień 9 STARY KOD, wyelminuj magiczne liczby
//
//        // Logowanie czasu wykonania
//        if (time > 50) {
//            AuditLogger.INSTANCE.logAdmin(user.getEmail(), "SLOW_REPORT", "Czas generowania: " + time + "ms");
//        } else {
//            System.out.println("Proxy [Timing]: Wykonano w " + time + "ms");
//        }


        // Tydzień 9 ZAD8 użyto nazwanej stałej zamiast wpisanej "na sztywno" wartości 50
        // Logowanie czasu wykonania
        if (time > SLOW_EXECUTION_THRESHOLD_MS) {
            AuditLogger.INSTANCE.logAdmin(user.getEmail(), "SLOW_REPORT", "Czas generowania: " + time + "ms");
        } else {
            System.out.println("Proxy [Timing]: Wykonano w " + time + "ms");
        }

        return stats;
    }
}