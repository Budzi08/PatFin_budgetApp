package com.patrykb.PatFin.service;

import com.patrykb.PatFin.dto.TransactionDto;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.model.enums.TransactionType;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

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
        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setDescription(dto.getDescription());
        transaction.setDate(dto.getDate());
        transaction.setType(dto.getType());
        transaction.setUser(user);
        if (dto.getCategoryId() != null) {
            Category category = categoryService.findById(dto.getCategoryId());
            transaction.setCategory(category);
        }
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAllByUserWithFilters(User user, LocalDate startDate, LocalDate endDate,
                                                    BigDecimal minAmount, BigDecimal maxAmount,
                                                    TransactionType type, Long categoryId) {
        // Pobierz wszystkie transakcje użytkownika (podstawowe query bez filtrów)
        List<Transaction> transactions = transactionRepository.findTransactionsWithFilters(
            user, startDate, endDate, minAmount, maxAmount, type, categoryId);
        
        // Filtrowanie po stronie aplikacji
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
        // Sprawdzamy czy transakcja należy do użytkownika przed usunięciem
        Transaction transaction = transactionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        if (!transaction.getUser().equals(user)) {
            throw new RuntimeException("Unauthorized to delete this transaction");
        }
        
        transactionRepository.deleteById(id);
    }

}
