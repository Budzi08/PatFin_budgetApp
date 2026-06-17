package com.patrykb.PatFin.tydzien8;

import com.patrykb.PatFin.model.Transaction;
import java.math.BigDecimal;
import java.math.RoundingMode;


//Klasa pochodna - dostarcza kwotę zaokrągloną do pełnych liczb

public class RoundedAmountProvider extends AmountProvider {
    @Override
    public BigDecimal getAmount(Transaction transaction) {
        // Pobieramy kwotę z klasy bazowej i tylko modyfikujemy wynik
        BigDecimal exactAmount = super.getAmount(transaction);
        return exactAmount.setScale(0, RoundingMode.HALF_UP);
    }
}