package com.patrykb.PatFin.pattern.decorator;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import java.util.List;

public class CachedTransactionSource extends TransactionDataSourceDecorator {
    private List<Transaction> cachedData;
    private User lastUser;

    public CachedTransactionSource(TransactionDataSource wrappedSource) {
        super(wrappedSource);
    }

    @Override
    public List<Transaction> fetchTransactions(User user) {
        if (cachedData != null && user.equals(lastUser)) {
            System.out.println("Returning cached transactions.");
            return cachedData;
        }
        cachedData = super.fetchTransactions(user);
        lastUser = user;
        return cachedData;
    }
}