package com.patrykb.PatFin.pattern.command;

import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.pattern.facade.UserOnboardingFacade;

// L5 Command #3 Proces logowania
public class UserOnboardingCommand implements FinancialCommand {
    private final UserOnboardingFacade onboardingFacade;
    private final String email;
    private final String password;
    private User result;

    public UserOnboardingCommand(UserOnboardingFacade facade, String email, String password) {
        this.onboardingFacade = facade;
        this.email = email;
        this.password = password;
    }

    @Override
    public void execute() {
        this.result = onboardingFacade.onboardNewUser(email, password);
        System.out.println("L5 Command #3: Przeprowadzono onboarding użytkownika przez obiekt Command");
    }

    public User getResult() {
        return result;
    }
}