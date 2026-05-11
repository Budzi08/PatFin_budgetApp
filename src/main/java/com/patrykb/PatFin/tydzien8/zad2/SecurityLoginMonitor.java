package com.patrykb.PatFin.tydzien8.zad2;
import org.springframework.stereotype.Component;

@Component
public class SecurityLoginMonitor extends AbstractLoginMonitor {
    @Override
    public void checkLoginAttempt(String email) {
        if (isSystemAccount(email)) {
            System.out.println("Wykryto próbę logowania na konto administratora: " + email);
        } else {
            System.out.println("Standardowe logowanie użytkownika: " + email);
        }
    }
}