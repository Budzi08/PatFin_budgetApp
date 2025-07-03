package com.patrykb.PatFin.repository;
import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUser(User user);
}
