package com.patrykb.PatFin.pattern.decorator;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.service.TransactionService;
import java.util.List;

public class DatabaseTransactionSource implements TransactionDataSource {
    private final TransactionService transactionService;

    public DatabaseTransactionSource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public List<Transaction> fetchTransactions(User user) {
        return transactionService.findAllByUser(user);
    }
}