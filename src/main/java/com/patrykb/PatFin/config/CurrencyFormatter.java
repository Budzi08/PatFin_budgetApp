package com.patrykb.PatFin.config;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private Locale locale = new Locale("pl", "PL");

    private CurrencyFormatter() {
    }

    private static class Holder {
        private static final CurrencyFormatter INSTANCE = new CurrencyFormatter();
    }

    public static CurrencyFormatter getInstance() {
        return Holder.INSTANCE;
    }

    public String format(BigDecimal amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        return currencyFormat.format(amount);
    }

    public String formatWithSign(BigDecimal amount, boolean isIncome) {
        String formatted = format(amount);
        return isIncome ? "+" + formatted : "-" + formatted;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
