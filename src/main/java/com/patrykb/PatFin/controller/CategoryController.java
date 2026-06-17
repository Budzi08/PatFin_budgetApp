package com.patrykb.PatFin.controller;

import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.pattern.iterator.PatFinIterator;
import com.patrykb.PatFin.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.math.BigDecimal;


import com.patrykb.PatFin.pattern.composite.BudgetGroup;
import com.patrykb.PatFin.pattern.composite.CategoryBudget;
import com.patrykb.PatFin.pattern.proxy.AuditCategoryProxy;
import com.patrykb.PatFin.tydzien8.zad2.CategoryValidator;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private AuditCategoryProxy auditProxy;

    @Autowired
    private CategoryService categoryService;

// Tydzień 9 STARY KOD, dostosuj 3 funkcje tak by spełniały tylko jedną rolę
//    @GetMapping
//    public List<Category> getAll() {
//        List<Category> categories = categoryService.findAll();
//
//        // WZORZEC: Composite (Use 3)
//        BudgetGroup masterBudget = new BudgetGroup();
////        for (Category c : categories) {
////            masterBudget.add(new CategoryBudget(new BigDecimal("500.00")));
////        }
//
//        // L5 Iterator #2
//        PatFinIterator<Category> catIt = new PatFinIterator<>() {
//            private int cursor = 0;
//            public boolean hasNext() { return cursor < categories.size(); }
//            public Category next() { return categories.get(cursor++); }
//        };
//
//        while (catIt.hasNext()) {
//            Category c = catIt.next(); // Pobieramy kategorię przez iterator
//            masterBudget.add(new CategoryBudget(new BigDecimal("500.00")));
//        }
//
//        System.out.println("Zasymulowany łączny limit budżetu kategorii: " + masterBudget.getBudgetLimit() + " PLN");
//
//        return categories;
//    }

    // Tydzień 9 ZAD 3.1 dostosuj 3 funkcje tak by spełniały tylko jedną rolę
    @GetMapping
    public List<Category> getAll() {
        List<Category> categories = categoryService.findAll();
        simulateBudgetLimits(categories);
        return categories;
    }

    // Tydzień 9 ZAD 3.1 dostosuj 3 funkcje tak by spełniały tylko jedną rolę - funkcja wydzielona dla SRP
    private void simulateBudgetLimits(List<Category> categories) {
        // WZORZEC: Composite (Use 3)
        BudgetGroup masterBudget = new BudgetGroup();
//        for (Category c : categories) {
//            masterBudget.add(new CategoryBudget(new BigDecimal("500.00")));
//        }

        // L5 Iterator #2
        PatFinIterator<Category> catIt = new PatFinIterator<>() {
            private int cursor = 0;
            public boolean hasNext() { return cursor < categories.size(); }
            public Category next() { return categories.get(cursor++); }
        };

        while (catIt.hasNext()) {
            Category c = catIt.next(); // Pobieramy kategorię przez iterator
            masterBudget.add(new CategoryBudget(new BigDecimal("500.00")));
        }

        System.out.println("Zasymulowany łączny limit budżetu kategorii: " + masterBudget.getBudgetLimit() + " PLN");
    }

    @Autowired
    private CategoryValidator categoryValidator;

    
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public Category add(@RequestBody Category category) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        //return categoryService.save(category);
        
        //wywołanie walidatora - kontroler nie widzi szczegółów implementacji
        categoryValidator.validate(category);

        // WZORZEC: Proxy - automatyczne logowanie przy tworzeniu kategorii przez administratora
        return auditProxy.saveAndAudit(category, email);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public List<Category> getCategoriesForAdmin() {
        return categoryService.findAll();
    }
}
