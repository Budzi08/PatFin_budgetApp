package com.patrykb.PatFin.pattern.facade;

import com.patrykb.PatFin.dto.TransactionDto;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.service.TransactionService;
import com.patrykb.PatFin.service.CategoryService;
import com.patrykb.PatFin.config.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinancialOperationFacade {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CategoryService categoryService;

    public Transaction registerTransaction(TransactionDto dto, User user) {
        // Fasada ukrywa proces weryfikacji kategorii przed kontrolerem
        if (dto.getCategoryId() != null) {
            categoryService.findById(dto.getCategoryId()); 
        }

        Transaction saved = transactionService.save(dto, user);
        
        // Automatyczny audyt po operacji
        AuditLogger.INSTANCE.logTransaction(user.getEmail(), "REGISTER_VIA_FACADE", saved.getId());
        
        return saved;
    }
}