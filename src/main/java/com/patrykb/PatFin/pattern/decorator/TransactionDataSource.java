package com.patrykb.PatFin.pattern.decorator;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import java.util.List;

public interface TransactionDataSource {
    List<Transaction> fetchTransactions(User user);
}