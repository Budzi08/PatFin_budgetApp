package com.patrykb.PatFin.service;

import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class TransactionFactory {

    /**
     * Wewnętrzny kontrakt
     */
    private interface Builder {
        Transaction build(BigDecimal amount, String description, LocalDate date,
                          Category category, User user);
    }

    private static final Builder INCOME_BUILDER = (amount, description, date, category, user) ->
            Transaction.builder()
                    .amount(amount.abs())
                    .description(description)
                    .date(date)
                    .type(TransactionType.INCOME)
                    .category(category)
                    .user(user)
                    .build();

    private static final Builder EXPENSE_BUILDER = (amount, description, date, category, user) ->
            Transaction.builder()
                    .amount(amount.abs())
                    .description(description)
                    .date(date)
                    .type(TransactionType.EXPENSE)
                    .category(category)
                    .user(user)
                    .build();

    /**
     * Tworzy transakcję odpowiedniego typu.
     * Jedyna metoda publiczna – całość odpowiedzialności klasy.
     */
    public Transaction create(TransactionType type, BigDecimal amount, String description,
                              LocalDate date, Category category, User user) {
        Builder builder = switch (type) {
            case INCOME  -> INCOME_BUILDER;
            case EXPENSE -> EXPENSE_BUILDER;
        };
        return builder.build(amount, description, date, category, user);
    }
}
