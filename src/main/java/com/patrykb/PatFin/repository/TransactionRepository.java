package com.patrykb.PatFin.repository;
import com.patrykb.PatFin.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.patrykb.PatFin.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);
    List<Transaction> findAll();
}

