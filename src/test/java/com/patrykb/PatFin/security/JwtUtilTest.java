package com.patrykb.PatFin.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
    }
    // Klasa 1 test 1
    // Testuje Generowanie tokena JWT.
    // Weryfikuje, czy metoda generateToken zwraca niepusty ciąg znaków dla poprawnych danych.
    @Test
    void shouldGenerateNonNullToken() {
        String token = jwtUtil.generateToken("test@test.com", false);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }
    // Klasa 1 test 2
    // Testuje Strukturę wygenerowanego tokena JWT.
    // Sprawdza, czy wygenerowany ciąg znaków składa się z trzech części oddzielonych kropkami (header, payload, signature), zgodnie ze standardem JWT.
    @Test
    void shouldGenerateTokenWithThreeParts() {
        String token = jwtUtil.generateToken("user@test.com", true);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "Token JWT powinien składać się z 3 części");
    }
    // Klasa 1 test 3
    // Testuje Walidację poprawnego tokena.
    // Sprawdza, czy świeżo wygenerowany token jest uznawany za ważny (validateToken przyjmuje 1 argument i zwraca true).
    @Test
    void shouldValidateCorrectToken() {
        String token = jwtUtil.generateToken("user@test.com", false);
        boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid);
    }
    // Klasa 1 test 4
    // Testuje Odrzucenie zmodyfikowanego (sfałszowanego) tokena.
    // Dodaje nieautoryzowane znaki na końcu poprawnego tokena i weryfikuje, czy metoda walidująca prawidłowo go odrzuci (zwróci false lub zablokuje dostęp).
    @Test
    void shouldNotValidateTamperedToken() {
        String token = jwtUtil.generateToken("user@test.com", false);
        String tamperedToken = token + "xyz"; // symulacja ingerencji w token

        try {
            boolean isValid = jwtUtil.validateToken(tamperedToken);
            assertFalse(isValid);
        } catch (Exception e) {
            // Jeśli implementacja JwtUtil rzuca wyjątek przy sfałszowanej sygnaturze, test również przechodzi pomyślnie
            assertTrue(true);
        }
    }
    // Klasa 1 test 5
    // Testuje Zachowanie walidatora dla pustych danych.
    // Sprawdza odporność metody validateToken na skrajne przypadki, przekazując pusty ciąg znaków.
    @Test
    void shouldHandleEmptyTokenGracefully() {
        try {
            boolean isValid = jwtUtil.validateToken("");
            assertFalse(isValid);
        } catch (Exception e) {
            // Jeśli metoda rzuca wyjątek (np. IllegalArgumentException), test uznajemy za zaliczony
            assertTrue(true);
        }
    }
}