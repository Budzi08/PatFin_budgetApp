package com.patrykb.PatFin.pattern.facade;

import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.pattern.mediator.PatFinMediator;
import com.patrykb.PatFin.service.UserService;
import com.patrykb.PatFin.service.CategoryService;
import com.patrykb.PatFin.config.AuditLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Component
public class UserOnboardingFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    // L5 Mediator #1
    @Autowired
    private PatFinMediator mediator;

    @Transactional
    public User onboardNewUser(String email, String password) {
        // 1. Rejestracja użytkownika
        User user = userService.registerUser(email, password);

//        // 2. Tworzenie domyślnych kategorii
//        ensureCategoryExists("Jedzenie");
//        ensureCategoryExists("Transport");
//        ensureCategoryExists("Rozrywka");
//
//        // 3. Logowanie zdarzenia
//        AuditLogger.INSTANCE.logAuth(email, "FULL_ONBOARDING_SUCCESS");

        // L5 Mediator #1
        mediator.notify(this, "USER_ONBOARDED");

        return user;
    }

    private void ensureCategoryExists(String name) {
        // Sprawdzamy, czy kategoria o tej nazwie już jest
        Optional<Category> existing = categoryService.findByName(name);
        
        if (existing.isEmpty()) {
            Category c = new Category();
            c.setName(name);
            categoryService.save(c); // Tworzymy nową tylko w razie braku
            System.out.println("Fasada: Utworzono nową kategorię: " + name);
        }
    }
}