package com.patrykb.PatFin.controller;

import com.patrykb.PatFin.dto.TransactionDto;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public List<Transaction> getTransactions() {
        return transactionService.findAll();
    }

    @PostMapping
    public Transaction addTransaction(@RequestBody TransactionDto dto) {
        // TransactionDto to obiekt do pobierania danych z frontu
        return transactionService.save(dto);
    }

}
