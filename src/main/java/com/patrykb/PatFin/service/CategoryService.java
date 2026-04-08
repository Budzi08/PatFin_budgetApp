package com.patrykb.PatFin.service;

import com.patrykb.PatFin.config.AppConfig;
import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.pattern.mediator.PatFinMediator;
import com.patrykb.PatFin.pattern.memento.ConfigMemento;
import com.patrykb.PatFin.repository.CategoryRepository;
import com.patrykb.PatFin.config.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // L5 Mediator #3
    @Autowired
    @Lazy
    private PatFinMediator mediator;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Category save(Category category) {
//        Category saved = categoryRepository.save(category);
//        AuditLogger.INSTANCE.logAdmin("SYSTEM", "CATEGORY_CREATE", "Kategoria: " + saved.getName());
//        return saved;

        // L5 Mediator #3 Zarządzanie Kategoriami
        Category saved = categoryRepository.save(category);
        mediator.notify(this, "CATEGORY_CREATED");
        return saved;
    }

    public Category cloneCategory(Long id, String newName) {
//        Category original = findById(id);
//        Category cloned = original.clone(); // Wzorzec Prototype
//        cloned.setName(newName);
//        return categoryRepository.save(cloned);

        AppConfig config = AppConfig.getInstance();

        // L5 Memento #3 - Robimy snapshot ustawień systemowych przed operacją na prototypie
        ConfigMemento backup = config.save();

        try {
            Category original = findById(id);
            Category cloned = original.clone();
            cloned.setName(newName);
            return categoryRepository.save(cloned);
        } catch (Exception e) {
            // W razie błędu przywracamy stan systemu
            config.restore(backup);
            throw e;
        }
    }

    public void deleteById(Long id) {
        AuditLogger.INSTANCE.logAdmin("SYSTEM", "CATEGORY_DELETE", "ID: " + id);
        categoryRepository.deleteById(id);
    }

    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }   
}
