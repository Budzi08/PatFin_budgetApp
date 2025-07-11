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
        return transactionService.findAllByUser(user);
    }

    @PostMapping
    public Transaction addTransaction(@RequestBody TransactionDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        return transactionService.save(dto, user);
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

}
