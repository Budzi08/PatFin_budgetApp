package com.patrykb.PatFin.repository;
import com.patrykb.PatFin.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {

}
