package com.patrykb.PatFin.service;

import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.enums.TransactionType;
import com.patrykb.PatFin.dto.TransactionDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class TransactionFactory {

    /**
     * Wewnętrzny kontrakt
     */
// Tydzień 9 STARY KOD, dostosuj 3 funkcje tak, by przyjmowały maksymalnie 3 argumenty
//    private interface Builder {
//        Transaction build(BigDecimal amount, String description, LocalDate date,
//                          Category category, User user);
//    }
//
//    private static final Builder INCOME_BUILDER = (amount, description, date, category, user) ->
//            Transaction.builder()
//                    .amount(amount.abs())
//                    .description(description)
//                    .date(date)
//                    .type(TransactionType.INCOME)
//                    .category(category)
//                    .user(user)
//                    .build();
//
//    private static final Builder EXPENSE_BUILDER = (amount, description, date, category, user) ->
//            Transaction.builder()
//                    .amount(amount.abs())
//                    .description(description)
//                    .date(date)
//                    .type(TransactionType.EXPENSE)
//                    .category(category)
//                    .user(user)
//                    .build();

    // Tydzień 9 ZAD5.2 dostosowano implementację lambd do zredukowanych parametrów nowego interfejsu Builder
    private interface Builder {
        Transaction build(TransactionDto dto, Category category, User user);
    }

    private static final Builder INCOME_BUILDER = (dto, category, user) ->
            Transaction.builder()
                    .amount(dto.getAmount().abs())
                    .description(dto.getDescription())
                    .date(dto.getDate())
                    .type(TransactionType.INCOME)
                    .category(category)
                    .user(user)
                    .build();

    private static final Builder EXPENSE_BUILDER = (dto, category, user) ->
            Transaction.builder()
                    .amount(dto.getAmount().abs())
                    .description(dto.getDescription())
                    .date(dto.getDate())
                    .type(TransactionType.EXPENSE)
                    .category(category)
                    .user(user)
                    .build();

    /**
     * Tworzy transakcję odpowiedniego typu.
     * Jedyna metoda publiczna – całość odpowiedzialności klasy.
     */
// Tydzień 9 STARY KOD, dostosuj 3 funkcje tak, by przyjmowały maksymalnie 3 argumenty - 3 pkt.
//    public Transaction create(TransactionType type, BigDecimal amount, String description,
//                              LocalDate date, Category category, User user) {
//        Builder builder = switch (type) {
//            case INCOME  -> INCOME_BUILDER;
//            case EXPENSE -> EXPENSE_BUILDER;
//        };
//        return builder.build(amount, description, date, category, user);
//    }

    // Tydzień 9 ZAD5.3 MIEJSCE 3 - zredukowano liczbę argumentów publicznej metody create z 6 do 3 za pomocą TransactionDto.
    public Transaction create(TransactionDto dto, Category category, User user) {
        Builder builder = switch (dto.getType()) {
            case INCOME  -> INCOME_BUILDER;
            case EXPENSE -> EXPENSE_BUILDER;
        };
        return builder.build(dto, category, user);
    }
}
