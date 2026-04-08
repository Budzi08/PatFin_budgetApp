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

    public Map<String, Object> getFormattedFinancialSummary(User user) {
        StatisticsDto.OverallStats stats = statisticsService.getOverallStats(user);
        CurrencyFormatter formatter = CurrencyFormatter.getInstance();
        AppConfig config = AppConfig.getInstance();

        // L5 Memento #3 zapis stanu przed zmianami
        ConfigMemento checkpoint = config.save();

        Map<String, Object> report = new HashMap<>();
        
        // Łączymy surowe dane z logiką formatowania i konfiguracją waluty
        //report.put("balanceFormatted", formatter.format(stats.getCurrentBalance()));


        // Użycie pyłka walutowego do pobrania symbolu i nazwy waluty, zamiast hardcodowania:
        CurrencyFlyweight currency = CurrencyFlyweightFactory.getCurrency(config.getDefaultCurrency());
        report.put("currencySymbol", currency.symbol()); // "zł" pobrane z pyłka
        report.put("currencyName", currency.fullName()); // "Polski Złoty" pobrane z pyłka

        report.put("incomeFormatted", formatter.formatWithSign(stats.getTotalIncome(), true));
        report.put("expenseFormatted", formatter.formatWithSign(stats.getTotalExpenses(), false));
        report.put("currency", config.getDefaultCurrency());
        report.put("transactionCount", stats.getTotalTransactions());

        // 2. L5 Memento #3 Przywracany stan
        // Dzięki temu jest pewność, że tymczasowe zmiany w raporcie nie wyciekły do reszty systemu
        config.restore(checkpoint);
        
        return report;
    }
}