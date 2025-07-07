package com.patrykb.PatFin.repository;
import com.patrykb.PatFin.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.math.BigDecimal;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.enums.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);
    
    // Statystyki per kategoria
    @Query("SELECT c.name, SUM(t.amount), COUNT(t) " +
           "FROM Transaction t JOIN t.category c " +
           "WHERE t.user = :user AND t.type = :type " +
           "GROUP BY c.id, c.name " +
           "ORDER BY SUM(t.amount) DESC")
    List<Object[]> findAmountByCategoryAndType(@Param("user") User user, @Param("type") TransactionType type);
    
    // Statystyki miesięczne
    @Query("SELECT YEAR(t.date), MONTH(t.date), t.type, SUM(t.amount) " +
           "FROM Transaction t " +
           "WHERE t.user = :user " +
           "GROUP BY YEAR(t.date), MONTH(t.date), t.type " +
           "ORDER BY YEAR(t.date) DESC, MONTH(t.date) DESC")
    List<Object[]> findMonthlyStatsByUser(@Param("user") User user);
    
    // Suma wszystkich przychodów/wydatków
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = :type")
    BigDecimal findTotalAmountByUserAndType(@Param("user") User user, @Param("type") TransactionType type);
    
    // Liczba transakcji użytkownika
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.user = :user")
    Long countByUser(@Param("user") User user);
}

