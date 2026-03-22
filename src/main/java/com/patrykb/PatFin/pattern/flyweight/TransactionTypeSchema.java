package com.patrykb.PatFin.pattern.flyweight;

import java.math.BigDecimal;

/**
 * Pyłek 3: Schemat Typu Transakcji.
 * Definiuje matematyczny wpływ typu na saldo.
 */
public record TransactionTypeSchema(String label, BigDecimal multiplier) {
}