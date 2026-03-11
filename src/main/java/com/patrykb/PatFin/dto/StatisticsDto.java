package com.patrykb.PatFin.dto;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsDto {
    
    public static class CategoryStats {
        private String categoryName;
        private BigDecimal totalAmount;
        private Long transactionCount;

        public CategoryStats() {}

        public CategoryStats(String categoryName, BigDecimal totalAmount, Long transactionCount) {
            this.categoryName = categoryName;
            this.totalAmount = totalAmount;
            this.transactionCount = transactionCount;
        }

        // Getters and Setters
        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Long getTransactionCount() {
            return transactionCount;
        }

        public void setTransactionCount(Long transactionCount) {
            this.transactionCount = transactionCount;
        }
    }

    public static class MonthlyStats {
        private int year;
        private int month;
        private BigDecimal totalIncome;
        private BigDecimal totalExpenses;
        private BigDecimal balance;

        public MonthlyStats() {}

        public MonthlyStats(int year, int month, BigDecimal totalIncome, BigDecimal totalExpenses) {
            this.year = year;
            this.month = month;
            this.totalIncome = totalIncome;
            this.totalExpenses = totalExpenses;
            this.balance = totalIncome.subtract(totalExpenses);
        }

        // Getters and Setters
        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public BigDecimal getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(BigDecimal totalIncome) {
            this.totalIncome = totalIncome;
        }

        public BigDecimal getTotalExpenses() {
            return totalExpenses;
        }

        public void setTotalExpenses(BigDecimal totalExpenses) {
            this.totalExpenses = totalExpenses;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
    }

    public static class OverallStats {
        private BigDecimal totalIncome;
        private BigDecimal totalExpenses;
        private BigDecimal currentBalance;
        private Long totalTransactions;
        private List<CategoryStats> expensesByCategory;
        private List<CategoryStats> incomeByCategory;
        private List<MonthlyStats> monthlyStats;

        public OverallStats() {}

        public static class Builder {
            private BigDecimal totalIncome = BigDecimal.ZERO;
            private BigDecimal totalExpenses = BigDecimal.ZERO;
            private Long totalTransactions = 0L;
            private List<CategoryStats> expensesByCategory;
            private List<CategoryStats> incomeByCategory;
            private List<MonthlyStats> monthlyStats;

            public Builder totalIncome(BigDecimal totalIncome) {
                this.totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
                return this;
            }

            public Builder totalExpenses(BigDecimal totalExpenses) {
                this.totalExpenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;
                return this;
            }

            public Builder totalTransactions(Long totalTransactions) {
                this.totalTransactions = totalTransactions != null ? totalTransactions : 0L;
                return this;
            }

            public Builder expensesByCategory(List<CategoryStats> expensesByCategory) {
                this.expensesByCategory = expensesByCategory;
                return this;
            }

            public Builder incomeByCategory(List<CategoryStats> incomeByCategory) {
                this.incomeByCategory = incomeByCategory;
                return this;
            }

            public Builder monthlyStats(List<MonthlyStats> monthlyStats) {
                this.monthlyStats = monthlyStats;
                return this;
            }

            public OverallStats build() {
                OverallStats stats = new OverallStats();
                stats.totalIncome = this.totalIncome;
                stats.totalExpenses = this.totalExpenses;
                stats.currentBalance = this.totalIncome.subtract(this.totalExpenses);
                stats.totalTransactions = this.totalTransactions;
                stats.expensesByCategory = this.expensesByCategory;
                stats.incomeByCategory = this.incomeByCategory;
                stats.monthlyStats = this.monthlyStats;
                return stats;
            }
        }

        public static Builder builder() {
            return new Builder();
        }

        // Getters and Setters
        public BigDecimal getTotalIncome() {
            return totalIncome;
        }

        public void setTotalIncome(BigDecimal totalIncome) {
            this.totalIncome = totalIncome;
        }

        public BigDecimal getTotalExpenses() {
            return totalExpenses;
        }

        public void setTotalExpenses(BigDecimal totalExpenses) {
            this.totalExpenses = totalExpenses;
        }

        public BigDecimal getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(BigDecimal currentBalance) {
            this.currentBalance = currentBalance;
        }

        public Long getTotalTransactions() {
            return totalTransactions;
        }

        public void setTotalTransactions(Long totalTransactions) {
            this.totalTransactions = totalTransactions;
        }

        public List<CategoryStats> getExpensesByCategory() {
            return expensesByCategory;
        }

        public void setExpensesByCategory(List<CategoryStats> expensesByCategory) {
            this.expensesByCategory = expensesByCategory;
        }

        public List<CategoryStats> getIncomeByCategory() {
            return incomeByCategory;
        }

        public void setIncomeByCategory(List<CategoryStats> incomeByCategory) {
            this.incomeByCategory = incomeByCategory;
        }

        public List<MonthlyStats> getMonthlyStats() {
            return monthlyStats;
        }

        public void setMonthlyStats(List<MonthlyStats> monthlyStats) {
            this.monthlyStats = monthlyStats;
        }
    }
}
