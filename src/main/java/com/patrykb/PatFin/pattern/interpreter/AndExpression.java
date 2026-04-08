package com.patrykb.PatFin.pattern.interpreter;

import com.patrykb.PatFin.model.Transaction;

// L5 Interpreter #3 AndExpression
public class AndExpression implements TransactionExpression {
    private final TransactionExpression expr1;
    private final TransactionExpression expr2;

    public AndExpression(TransactionExpression expr1, TransactionExpression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public boolean interpret(Transaction context) {
        return expr1.interpret(context) && expr2.interpret(context);
    }
}