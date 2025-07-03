package com.patrykb.PatFin.service;

import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getUserCategories(User user) {
        return categoryRepository.findAllByUser(user);
    }

    public Category createCategory(String name, User user) {
        Category category = new Category();
        category.setName(name);
        category.setUser(user);
        return categoryRepository.save(category);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
