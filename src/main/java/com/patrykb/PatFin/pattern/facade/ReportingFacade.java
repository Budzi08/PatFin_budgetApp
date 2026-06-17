package com.patrykb.PatFin.pattern.facade;

import com.patrykb.PatFin.dto.StatisticsDto;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.pattern.memento.ConfigMemento;
import com.patrykb.PatFin.service.StatisticsService;
import com.patrykb.PatFin.config.CurrencyFormatter;
import com.patrykb.PatFin.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.patrykb.PatFin.pattern.flyweight.CurrencyFlyweight;
import com.patrykb.PatFin.pattern.flyweight.CurrencyFlyweightFactory;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReportingFacade {

    @Autowired
    private StatisticsService statisticsService;
// Tydzień 9 STARY KOD, dostosuj 3 funkcje tak by były tylko na jednym poziomie abstrakcji, kolejno wywoływane funkcje coraz bardziej szczegółowe (top to botom)
//    public Map<String, Object> getFormattedFinancialSummary(User user) {
//        StatisticsDto.OverallStats stats = statisticsService.getOverallStats(user);
//        CurrencyFormatter formatter = CurrencyFormatter.getInstance();
//        AppConfig config = AppConfig.getInstance();
//
//        // L5 Memento #3 zapis stanu przed zmianami
//        ConfigMemento checkpoint = config.save();
//
//        Map<String, Object> report = new HashMap<>();
//
//        // Łączymy surowe dane z logiką formatowania i konfiguracją waluty
//        //report.put("balanceFormatted", formatter.format(stats.getCurrentBalance()));
//
//
//        // Użycie pyłka walutowego do pobrania symbolu i nazwy waluty, zamiast hardcodowania:
//        CurrencyFlyweight currency = CurrencyFlyweightFactory.getCurrency(config.getDefaultCurrency());
//        report.put("currencySymbol", currency.symbol()); // "zł" pobrane z pyłka
//        report.put("currencyName", currency.fullName()); // "Polski Złoty" pobrane z pyłka
//
//        report.put("incomeFormatted", formatter.formatWithSign(stats.getTotalIncome(), true));
//        report.put("expenseFormatted", formatter.formatWithSign(stats.getTotalExpenses(), false));
//        report.put("currency", config.getDefaultCurrency());
//        report.put("transactionCount", stats.getTotalTransactions());
//
//        // 2. L5 Memento #3 Przywracany stan
//        // Dzięki temu jest pewność, że tymczasowe zmiany w raporcie nie wyciekły do reszty systemu
//        config.restore(checkpoint);
//
//        return report;
//    }

    // Tydzień 9 ZAD4.3 dostosuj 3 funkcje tak by były tylko na jednym poziomie abstrakcji (zastosowano Step-down Rule)
    public Map<String, Object> getFormattedFinancialSummary(User user) {
        AppConfig config = AppConfig.getInstance();
        ConfigMemento checkpoint = config.save();

        try {
            return generateReportData(user, config);
        } finally {
            config.restore(checkpoint);
        }
    }

    private Map<String, Object> generateReportData(User user, AppConfig config) {
        StatisticsDto.OverallStats stats = statisticsService.getOverallStats(user);
        Map<String, Object> report = new HashMap<>();

        appendCurrencyData(report, config.getDefaultCurrency());
        appendFinancialData(report, stats);

        return report;
    }

    private void appendCurrencyData(Map<String, Object> report, String defaultCurrencyCode) {
        CurrencyFlyweight currency = CurrencyFlyweightFactory.getCurrency(defaultCurrencyCode);
        report.put("currency", defaultCurrencyCode);
        report.put("currencySymbol", currency.symbol());
        report.put("currencyName", currency.fullName());
    }

    private void appendFinancialData(Map<String, Object> report, StatisticsDto.OverallStats stats) {
        CurrencyFormatter formatter = CurrencyFormatter.getInstance();
        report.put("incomeFormatted", formatter.formatWithSign(stats.getTotalIncome(), true));
        report.put("expenseFormatted", formatter.formatWithSign(stats.getTotalExpenses(), false));
    }
}