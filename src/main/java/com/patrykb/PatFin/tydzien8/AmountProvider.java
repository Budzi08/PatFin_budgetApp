package com.patrykb.PatFin.tydzien8;

import com.patrykb.PatFin.model.Transaction;
import java.math.BigDecimal;


//Klasa bazowa - dostarcza dokładną, surową kwotę transakcji

public class AmountProvider {
    public BigDecimal getAmount(Transaction transaction) {
        if (transaction == null || transaction.getAmount() == null) {
            return BigDecimal.ZERO;
        }
        return transaction.getAmount();
    }
}