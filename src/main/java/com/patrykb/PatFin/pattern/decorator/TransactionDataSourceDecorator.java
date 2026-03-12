package com.patrykb.PatFin.pattern.decorator;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import java.util.List;

public abstract class TransactionDataSourceDecorator implements TransactionDataSource {
    protected TransactionDataSource wrappedSource;

    public TransactionDataSourceDecorator(TransactionDataSource wrappedSource) {
        this.wrappedSource = wrappedSource;
    }

    @Override
    public List<Transaction> fetchTransactions(User user) {
        return wrappedSource.fetchTransactions(user);
    }
}