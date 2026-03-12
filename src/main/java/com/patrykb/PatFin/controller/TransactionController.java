package com.patrykb.PatFin.controller;

import com.patrykb.PatFin.dto.TransactionDto;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.service.TransactionService;
import com.patrykb.PatFin.service.UserService;
import com.patrykb.PatFin.model.enums.TransactionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.springframework.security.core.Authentication;
import com.patrykb.PatFin.model.User;

import com.patrykb.PatFin.pattern.decorator.TransactionDataSource;
import com.patrykb.PatFin.pattern.decorator.DatabaseTransactionSource;
import com.patrykb.PatFin.pattern.decorator.CachedTransactionSource;
import com.patrykb.PatFin.pattern.bridge.Alert;
import com.patrykb.PatFin.pattern.bridge.OverdraftAlert;
import com.patrykb.PatFin.pattern.bridge.EmailChannel;
import com.patrykb.PatFin.pattern.adapter.ExportableItem;
import com.patrykb.PatFin.pattern.adapter.TransactionExportAdapter;
import com.patrykb.PatFin.pattern.decorator.ExportWriter;
import com.patrykb.PatFin.pattern.decorator.SimpleExportWriter;
import com.patrykb.PatFin.pattern.decorator.HtmlExportWriterDecorator;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Transaction> getUserTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        // WZORZEC: Decorator (Use 1)
        TransactionDataSource source = new CachedTransactionSource(new DatabaseTransactionSource(transactionService));

        return transactionService.findAllByUser(user);
    }

    @PostMapping
    public Transaction addTransaction(@RequestBody TransactionDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        Transaction saved = transactionService.save(dto, user);

        // WZORZEC: Bridge (Use 2) - Wysyłamy alert przekroczenia budżetu, jeśli wydatek > 1000 PLN
        if (saved.getType() == TransactionType.EXPENSE && saved.getAmount().compareTo(new BigDecimal("1000")) > 0) {
            Alert alert = new OverdraftAlert(new EmailChannel());
            alert.trigger("Zarejestrowano bardzo wysoki wydatek: " + saved.getAmount());
        }

        return saved;
    }

    @GetMapping("/export")
    public String exportTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        List<Transaction> transactions = transactionService.findAllByUser(user);

        StringBuilder sb = new StringBuilder();
        for(Transaction t : transactions) {
            // WZORZEC: Adapter (Use 1)
            ExportableItem item = new TransactionExportAdapter(t);
            sb.append("Tytul: ").append(item.getExportTitle())
                    .append(" | Kwota: ").append(item.getExportValue()).append("<br/>");
        }

        // WZORZEC: Decorator (Use 2) - Dekorowanie wyjścia formatem HTML
        ExportWriter writer = new HtmlExportWriterDecorator(new SimpleExportWriter());
        return writer.write(sb.toString());
    }

    @GetMapping("/filter")
    public List<Transaction> getFilteredTransactions(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        
        return transactionService.findAllByUserWithFilters(
            user, startDate, endDate, minAmount, maxAmount, type, categoryId);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        transactionService.deleteById(id, user);
    }

    @PostMapping("/{id}/duplicate")
    public Transaction duplicateTransaction(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        return transactionService.duplicate(id, user);
    }

}
