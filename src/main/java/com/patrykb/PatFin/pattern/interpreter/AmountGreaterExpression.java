package com.patrykb.PatFin.pattern.interpreter;

import com.patrykb.PatFin.model.Transaction;
import java.math.BigDecimal;

// L5 Interpreter #1 Filtrowanie kwotowe
public class AmountGreaterExpression implements TransactionExpression {
    private final BigDecimal minAmount;

    public AmountGreaterExpression(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    @Override
    public boolean interpret(Transaction context) {
        return context.getAmount().compareTo(minAmount) >= 0;
    }
}