package com.patrykb.PatFin.pattern.proxy;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.service.TransactionService;
import com.patrykb.PatFin.repository.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class SecurityTransactionProxy {

    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public SecurityTransactionProxy(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    public void safeDelete(Long id, User currentUser) {
        // Logika ochrony: najpierw sprawdzamy dostęp, potem wywołujemy oryginał
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono transakcji"));

        if (!t.getUser().getId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
            throw new RuntimeException("Brak uprawnień: Nie jesteś właścicielem tej transakcji!");
        }

        transactionService.deleteById(id, currentUser);
    }
}