package com.patrykb.PatFin.service;

import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    // Klasa 4 test 1
    // Testuje Udany proces rejestracji.
    // Weryfikuje, że dla nowego adresu email hasło zostaje zahashowane, a użytkownik poprawnie zapisany w repozytorium.
    @Test
    void shouldRegisterNewUserSuccessfully() {
        String email = "nowy@test.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode("haslo123")).thenReturn("hashed_haslo");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User savedUser = userService.registerUser(email, "haslo123");

        assertEquals(email, savedUser.getEmail());
        assertEquals("hashed_haslo", savedUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }
    // Klasa 4 test 2
    // Testuje Blokadę rejestracji na zajęty email.
    // Sprawdza, czy w przypadku istnienia emaila w bazie, serwis rzuci oczekiwany wyjątek HTTP 409 CONFLICT (nasza modyfikacja Clean Code).
    @Test
    void shouldThrowConflictWhenEmailAlreadyExists() {
        String email = "zajety@test.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userService.registerUser(email, "haslo123");
        });

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(userRepository, never()).save(any());
    }
    // Klasa 4 test 3
    // Testuje Wyszukiwanie użytkownika po istniejącym emailu.
    // Sprawdza, czy serwis prawidłowo odpakowuje Optionala z repozytorium i zwraca obiekt User.
    @Test
    void shouldFindUserByEmailWhenExists() {
        User user = new User();
        user.setEmail("test@test.com");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("test@test.com");
        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
    }
    // Klasa 4 test 4
    // Testuje Wyszukiwanie użytkownika, który nie istnieje.
    // Sprawdza, czy dla nieznanego emaila (Optional.empty) serwis bezpiecznie zwróci wartość null.
    @Test
    void shouldReturnNullWhenUserNotFoundByEmail() {
        when(userRepository.findByEmail("nieznany@test.com")).thenReturn(Optional.empty());

        User result = userService.findByEmail("nieznany@test.com");
        assertNull(result);
    }
    // Klasa 4 test 5
    // Testuje Weryfikację zgodności haseł.
    // Weryfikuje, czy serwis poprawnie deleguje sprawdzanie hasła do wstrzykniętego komponentu PasswordEncoder.
    @Test
    void shouldDelegatePasswordCheckingToEncoder() {
        String raw = "haslo";
        String encoded = "zakodowane_haslo";
        when(passwordEncoder.matches(raw, encoded)).thenReturn(true);

        boolean result = userService.checkPassword(raw, encoded);
        assertTrue(result);
        verify(passwordEncoder, times(1)).matches(raw, encoded);
    }
}