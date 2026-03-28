package com.patrykb.PatFin.controller;

import com.patrykb.PatFin.model.Category;
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


@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private AuditCategoryProxy auditProxy;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAll() {
        List<Category> categories = categoryService.findAll();

        // WZORZEC: Composite (Use 3)
        BudgetGroup masterBudget = new BudgetGroup();
        for (Category c : categories) {
            masterBudget.add(new CategoryBudget(new BigDecimal("500.00")));
        }
        System.out.println("Zasymulowany łączny limit budżetu kategorii: " + masterBudget.getBudgetLimit() + " PLN");

        return categories;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public Category add(@RequestBody Category category) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) auth.getPrincipal();
        //return categoryService.save(category);
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
