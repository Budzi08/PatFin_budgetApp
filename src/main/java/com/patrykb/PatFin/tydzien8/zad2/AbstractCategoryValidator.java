package com.patrykb.PatFin.tydzien8.zad2;

public abstract class AbstractCategoryValidator implements CategoryValidator {
    // Wspólna metoda dla wszystkich walidatorów do rzucania błędów
    protected void throwValidationError(String message) {
        throw new RuntimeException("Błąd walidacji kategorii: " + message);
    }
}