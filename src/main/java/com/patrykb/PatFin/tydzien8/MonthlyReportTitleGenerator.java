package com.patrykb.PatFin.tydzien8;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


//Klasa pochodna - rozszerza funkcjonalność klasy bazowej, generując tytuł raportu z aktualnym miesiącem i rokiem.

public class MonthlyReportTitleGenerator extends ReportTitleGenerator {

    @Override
    public String generateTitle() {
        //rozszerzenie funkcjonalności klasy bazowej
        String baseTitle = super.generateTitle();
        String currentMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"));
        return baseTitle + " (" + currentMonth + ")";
    }
}