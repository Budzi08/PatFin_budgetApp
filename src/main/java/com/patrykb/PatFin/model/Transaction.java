package com.patrykb.PatFin.model;

import com.patrykb.PatFin.model.enums.TransactionType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private String description;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // INCOME / EXPENSE

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Transaction() {
    }

    public Transaction(BigDecimal amount, String description, LocalDate date, TransactionType type, Category category,
            User user) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.category = category;
        this.user = user;
    }

    @Override
    public Transaction clone() {
        try {
            Transaction cloned = (Transaction) super.clone();
            cloned.id = null;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Klonowanie Transaction nie powiodło się", e);
        }
    }

    public static class Builder {
        private BigDecimal amount;
        private String description;
        private LocalDate date;
        private TransactionType type;
        private Category category;
        private User user;

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

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Transaction build() {
            Transaction t = new Transaction();
            t.amount = this.amount != null ? this.amount.abs() : BigDecimal.ZERO;
            t.description = this.description;
            t.date = this.date != null ? this.date : LocalDate.now();
            t.type = this.type;
            t.category = this.category;
            t.user = this.user;
            return t;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
