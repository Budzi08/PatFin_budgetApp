package com.patrykb.PatFin.service;

import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.repository.CategoryRepository;
import com.patrykb.PatFin.config.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category save(Category category) {
        Category saved = categoryRepository.save(category);
        AuditLogger.INSTANCE.logAdmin("SYSTEM", "CATEGORY_CREATE", "Kategoria: " + saved.getName());
        return saved;
    }

    public Category cloneCategory(Long id, String newName) {
        Category original = findById(id);
        Category cloned = original.clone(); // Wzorzec Prototype
        cloned.setName(newName);
        return categoryRepository.save(cloned);
    }

    public void deleteById(Long id) {
        AuditLogger.INSTANCE.logAdmin("SYSTEM", "CATEGORY_DELETE", "ID: " + id);
        categoryRepository.deleteById(id);
    }
}
