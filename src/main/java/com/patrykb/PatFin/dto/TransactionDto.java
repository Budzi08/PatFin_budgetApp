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

    public static class Builder {
        private BigDecimal amount;
        private String description;
        private LocalDate date;
        private TransactionType type;
        private Long categoryId;

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder categoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public TransactionDto build() {
            return new TransactionDto(amount, description, date, type, categoryId);
        }
    }

    public static Builder builder() {
        return new Builder();
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
