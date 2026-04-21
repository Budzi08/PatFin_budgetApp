package com.patrykb.PatFin.service;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.enums.TransactionType;
import com.patrykb.PatFin.pattern.interpreter.AmountGreaterExpression;
import com.patrykb.PatFin.pattern.interpreter.AndExpression;
import com.patrykb.PatFin.pattern.interpreter.TransactionExpression;
import com.patrykb.PatFin.pattern.interpreter.TypeExpression;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionFilterService {

    /**
     * Filtruje podaną listę transakcji według opcjonalnych kryteriów.
     * Używa wzorca Interpreter (AndExpression) – dokładnie tak jak w oryginale,
     * ale w dedykowanej klasie.
     */
    public List<Transaction> filter(List<Transaction> transactions,
                                    BigDecimal minAmount,
                                    TransactionType type) {

        // wzorzec Interpreter
        TransactionExpression filter = t -> true;

        if (minAmount != null) {
            filter = new AndExpression(filter, new AmountGreaterExpression(minAmount));
        }
        if (type != null) {
            filter = new AndExpression(filter, new TypeExpression(type));
        }

        final TransactionExpression finalFilter = filter;
        return transactions.stream()
                .filter(finalFilter::interpret)
                .collect(Collectors.toList());
    }
}
