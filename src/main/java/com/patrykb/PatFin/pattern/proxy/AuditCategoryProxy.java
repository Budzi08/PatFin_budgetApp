package com.patrykb.PatFin.pattern.proxy;

import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.service.CategoryService;
import com.patrykb.PatFin.config.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditCategoryProxy {

    @Autowired
    private CategoryService categoryService;

    public Category saveAndAudit(Category category, String adminEmail) {
        Category saved = categoryService.save(category);
        
        //automatyczne wpis przy tworzeniu kategorii przez administratora
        AuditLogger.INSTANCE.logAdmin(adminEmail, "ADMIN_CREATE_CATEGORY", "Nazwa: " + saved.getName());
        
        return saved;
    }
}