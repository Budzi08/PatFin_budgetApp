package com.patrykb.PatFin.pattern.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionGroup implements FinancialElement {
    private final List<FinancialElement> elements = new ArrayList<>();

    public void addElement(FinancialElement element) {
        elements.add(element);
    }

    public void removeElement(FinancialElement element) {
        elements.remove(element);
    }

    @Override
    public BigDecimal calculateTotal() {
        return elements.stream()
                .map(FinancialElement::calculateTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}