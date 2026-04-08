package com.patrykb.PatFin.config;

import com.patrykb.PatFin.pattern.memento.ConfigMemento;

public class AppConfig {

    private static final AppConfig INSTANCE = new AppConfig();

    private String defaultCurrency = "PLN";
    private int maxTransactionsPerPage = 50;
    private String dateFormat = "yyyy-MM-dd";
    private double vatRate = 0.23;

    private AppConfig() {
    }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public int getMaxTransactionsPerPage() {
        return maxTransactionsPerPage;
    }

    public void setMaxTransactionsPerPage(int maxTransactionsPerPage) {
        this.maxTransactionsPerPage = maxTransactionsPerPage;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    // L5 Memento #1: Metoda tworząca migawkę stanu
    public ConfigMemento save() {
        System.out.println("Memento: Tworzę migawkę stanu konfiguracji.");
        return new ConfigMemento(this.defaultCurrency, this.vatRate);
    }

    // L5 Memento #2: Metoda przywracająca stan z migawki
    public void restore(ConfigMemento memento) {
        if (memento != null) {
        /*
        this.defaultCurrency = "PLN";
        this.vatRate = 23.0;
        */

            this.defaultCurrency = memento.getCurrency();
            this.vatRate = memento.getVatRate();
            System.out.println("Memento: Przywrócono stan z pamiątki.");
        }
    }
}
