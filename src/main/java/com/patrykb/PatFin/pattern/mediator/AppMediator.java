package com.patrykb.PatFin.pattern.mediator;

import com.patrykb.PatFin.config.AuditLogger;
import com.patrykb.PatFin.service.CategoryService;
import com.patrykb.PatFin.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Lazy;

@Component
public class AppMediator implements PatFinMediator {

    @Autowired
    @Lazy
    private CategoryService categoryService;

    @Override
    public void notify(Object sender, String event) {
        // L5 Mediator #3 (Logika centralna) Koordynacja Onboardingu
        if ("USER_ONBOARDED".equals(event)) {
            // Przeniesiona logika z UserOnboardingFacade
            System.out.println("Mediator: Automatyczna konfiguracja dla nowego użytkownika.");
            // Przykład: AuditLogger.INSTANCE.logAuth("SYSTEM", "MEDIATED_EVENT");
        }

        if ("TRANSACTION_DELETED".equals(event)) {
            // Reakcja na usunięcie transakcji
            System.out.println("Mediator: Zarejestrowano usunięcie transakcji.");
        }
    }
}