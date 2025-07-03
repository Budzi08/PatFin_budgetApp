package com.patrykb.PatFin.dto;

import com.patrykb.PatFin.model.enums.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDto {
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    private TransactionType type;
    private Long categoryId;

    public TransactionDto() {
    }

    public TransactionDto(BigDecimal amount, String description, LocalDate date, TransactionType type,
            Long categoryId) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.categoryId = categoryId;
    }

    // Getters and Setters
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

}
