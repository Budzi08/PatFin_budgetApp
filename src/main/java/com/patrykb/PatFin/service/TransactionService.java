package com.patrykb.PatFin.service;

import com.patrykb.PatFin.dto.TransactionDto;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.pattern.mediator.PatFinMediator;
import com.patrykb.PatFin.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.model.enums.TransactionType;

import com.patrykb.PatFin.config.AuditLogger;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;


@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryService categoryService;

    // SRP: tworzenie transakcji delegowane do dedykowanej klasy
    @Autowired
    private TransactionFactory transactionFactory;

    // SRP: filtrowanie wyników delegowane do dedykowanej klasy
    @Autowired
    private TransactionFilterService transactionFilterService;

    // L5 Mediator #2
    @Autowired
    private PatFinMediator mediator;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> findAllByUser(User user) {
        return transactionRepository.findAllByUser(user);
    }

    public Transaction save(TransactionDto dto, User user) {
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryService.findById(dto.getCategoryId());
        }

        // SRP: delegujemy tworzenie obiektu do TransactionFactory
        Transaction transaction = transactionFactory.create(
                dto.getType(), dto.getAmount(), dto.getDescription(),
                dto.getDate(), category, user);

        Transaction saved = transactionRepository.save(transaction);
        AuditLogger.INSTANCE.logTransaction(user.getEmail(), "CREATE", saved.getId());
        return saved;
    }

    public Transaction duplicate(Long transactionId, User user) {
        Transaction original = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if (!original.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        Transaction cloned = original.clone();
        cloned.setDate(LocalDate.now());
        return transactionRepository.save(cloned);
    }

    public List<Transaction> findAllByUserWithFilters(User user, LocalDate startDate, LocalDate endDate,
                                                      BigDecimal minAmount, BigDecimal maxAmount,
                                                      TransactionType type, Long categoryId) {

        List<Transaction> transactions = transactionRepository.findTransactionsWithFilters(
                user, startDate, endDate, minAmount, maxAmount, type, categoryId);

        // SRP: delegujemy filtrowanie do TransactionFilterService
        return transactionFilterService.filter(transactions, minAmount, type);
    }

    public void deleteById(Long id, User user) {
        transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transactionRepository.deleteById(id);

        // L5 Mediator #2 Reakcja na usunięcie
        mediator.notify(this, "TRANSACTION_DELETED");
    }
}

