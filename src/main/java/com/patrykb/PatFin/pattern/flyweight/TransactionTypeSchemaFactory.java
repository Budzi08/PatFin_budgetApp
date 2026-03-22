package com.patrykb.PatFin.pattern.flyweight;

import com.patrykb.PatFin.model.enums.TransactionType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransactionTypeSchemaFactory {
    private static final Map<TransactionType, TransactionTypeSchema> schemas = new HashMap<>();

    public static TransactionTypeSchema getSchema(TransactionType type) {
        return schemas.computeIfAbsent(type, t -> switch (t) {
            case INCOME -> new TransactionTypeSchema("Przychód", new BigDecimal("1"));
            case EXPENSE -> new TransactionTypeSchema("Wydatek", new BigDecimal("-1"));
        });
    }
}