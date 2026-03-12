package com.patrykb.PatFin.pattern.adapter;

import com.patrykb.PatFin.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionExportAdapter implements ExportableItem {
    private final Transaction transaction;

    public TransactionExportAdapter(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String getExportTitle() {
        return transaction.getDescription() != null ? transaction.getDescription() : "Brak opisu";
    }

    @Override
    public BigDecimal getExportValue() {
        return transaction.getAmount();
    }

    @Override
    public LocalDate getExportDate() {
        return transaction.getDate();
    }
}