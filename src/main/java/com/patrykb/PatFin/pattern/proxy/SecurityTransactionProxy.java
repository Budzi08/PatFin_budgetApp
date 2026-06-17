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

// Tydzień 9 STARY KOD, dostosuj 3 funkcje tak by spełniały tylko jedną rolę
//    public void safeDelete(Long id, User currentUser) {
//        // Logika ochrony: najpierw sprawdzamy dostęp, potem wywołujemy oryginał
//        Transaction t = transactionRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Nie znaleziono transakcji"));
//
//        if (!t.getUser().getId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
//            throw new RuntimeException("Brak uprawnień: Nie jesteś właścicielem tej transakcji!");
//        }
//
//        transactionService.deleteById(id, currentUser);
//    }

    // Tydzień 9 ZAD3.3 dostosuj 3 funkcje tak by spełniały tylko jedną rolę
    public void safeDelete(Long id, User currentUser) {
        // Logika ochrony: najpierw sprawdzamy dostęp, potem wywołujemy oryginał
        Transaction t = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono transakcji"));

        verifyOwnershipOrAdmin(t, currentUser);

        transactionService.deleteById(id, currentUser);
    }

    // Tydzień 9 ZAD3.3 dostosuj 3 funkcje tak by spełniały tylko jedną rolę - funkcja wydzielona dla SRP
    private void verifyOwnershipOrAdmin(Transaction t, User currentUser) {
        if (!t.getUser().getId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
            throw new RuntimeException("Brak uprawnień: Nie jesteś właścicielem tej transakcji!");
        }
    }
}