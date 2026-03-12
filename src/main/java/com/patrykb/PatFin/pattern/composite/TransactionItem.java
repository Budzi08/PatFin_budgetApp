package com.patrykb.PatFin.pattern.composite;

import com.patrykb.PatFin.model.Transaction;
import java.math.BigDecimal;

public class TransactionItem implements FinancialElement {
    private final Transaction transaction;

    public TransactionItem(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public BigDecimal calculateTotal() {
        return transaction.getAmount();
    }
}