package com.patrykb.PatFin.tydzien8.zad2;

public abstract class AbstractLoginMonitor implements LoginMonitor {
    // Logika wspólna - sprawdzanie, czy mail wygląda na służbowy/systemowy
    protected boolean isSystemAccount(String email) {
        return email != null && email.toLowerCase().contains("admin");
    }
}