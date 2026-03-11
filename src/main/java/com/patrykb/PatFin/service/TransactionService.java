package com.patrykb.PatFin.service;

import com.patrykb.PatFin.dto.TransactionDto;
import com.patrykb.PatFin.model.Transaction;
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

    interface TransactionFactory {
        Transaction create(BigDecimal amount, String description, LocalDate date,
                          Category category, User user);
    }

    static class IncomeFactory implements TransactionFactory {
        @Override
        public Transaction create(BigDecimal amount, String description, LocalDate date,
                                  Category category, User user) {
            return Transaction.builder()
                    .amount(amount.abs())
                    .description(description)
                    .date(date)
                    .type(TransactionType.INCOME)
                    .category(category)
                    .user(user)
                    .build();
        }
    }

    static class ExpenseFactory implements TransactionFactory {
        @Override
        public Transaction create(BigDecimal amount, String description, LocalDate date,
                                  Category category, User user) {
            return Transaction.builder()
                    .amount(amount.abs())
                    .description(description)
                    .date(date)
                    .type(TransactionType.EXPENSE)
                    .category(category)
                    .user(user)
                    .build();
        }
    }

    private TransactionFactory getFactory(TransactionType type) {
        return switch (type) {
            case INCOME -> new IncomeFactory();
            case EXPENSE -> new ExpenseFactory();
        };
    }

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryService categoryService;

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

        TransactionFactory factory = getFactory(dto.getType());
        Transaction transaction = factory.create(
                dto.getAmount(), dto.getDescription(), dto.getDate(), category, user);

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
        

        return transactions.stream()
            .filter(t -> startDate == null || !t.getDate().isBefore(startDate))
            .filter(t -> endDate == null || !t.getDate().isAfter(endDate))
            .filter(t -> minAmount == null || t.getAmount().compareTo(minAmount) >= 0)
            .filter(t -> maxAmount == null || t.getAmount().compareTo(maxAmount) <= 0)
            .filter(t -> type == null || t.getType().equals(type))
            .filter(t -> categoryId == null || (t.getCategory() != null && t.getCategory().getId().equals(categoryId)))
            .collect(java.util.stream.Collectors.toList());
    }

    public void deleteById(Long id, User user) {
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        if (!transaction.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized to delete this transaction");
        }

        AuditLogger.INSTANCE.logTransaction(user.getEmail(), "DELETE", id);
        transactionRepository.deleteById(id);
    }

}
