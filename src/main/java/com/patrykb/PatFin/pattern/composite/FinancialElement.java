package com.patrykb.PatFin.pattern.composite;

import java.math.BigDecimal;

public interface FinancialElement {
    BigDecimal calculateTotal();
}