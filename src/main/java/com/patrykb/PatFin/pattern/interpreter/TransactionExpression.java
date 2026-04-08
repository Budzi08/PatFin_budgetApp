package com.patrykb.PatFin.pattern.interpreter;

import com.patrykb.PatFin.model.Transaction;

// L5 Interpreter #1
public interface TransactionExpression {
    boolean interpret(Transaction context);
}