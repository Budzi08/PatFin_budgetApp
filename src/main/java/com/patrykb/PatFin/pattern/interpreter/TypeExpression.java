package com.patrykb.PatFin.pattern.interpreter;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.enums.TransactionType;

// L5 Interpreter #2 Filtrowanie typologiczne
public class TypeExpression implements TransactionExpression {
    private final TransactionType type;

    public TypeExpression(TransactionType type) {
        this.type = type;
    }

    @Override
    public boolean interpret(Transaction context) {
        return context.getType() == type;
    }
}