package com.patrykb.PatFin.pattern.memento;

// L5 Memento #1 (Klasa przechowująca stan)
public class ConfigMemento {
    private final String currency;
    private final double vatRate;

    public ConfigMemento(String currency, double vatRate) {
        this.currency = currency;
        this.vatRate = vatRate;
    }

    public String getCurrency() { return currency; }
    public double getVatRate() { return vatRate; }
}