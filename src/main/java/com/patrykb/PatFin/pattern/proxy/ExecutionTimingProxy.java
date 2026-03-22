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

    @Autowired
    private StatisticsService statisticsService;

    public StatisticsDto.OverallStats getStatsWithTiming(User user) {
        StopWatch sw = new StopWatch();
        sw.start();

        StatisticsDto.OverallStats stats = statisticsService.getOverallStats(user);

        sw.stop();
        long time = sw.getTotalTimeMillis();

        // Logowanie czasu wykonania
        if (time > 50) {
            AuditLogger.INSTANCE.logAdmin(user.getEmail(), "SLOW_REPORT", "Czas generowania: " + time + "ms");
        } else {
            System.out.println("Proxy [Timing]: Wykonano w " + time + "ms");
        }

        return stats;
    }
}