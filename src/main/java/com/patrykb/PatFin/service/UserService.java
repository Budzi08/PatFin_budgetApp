package com.patrykb.PatFin.service;

import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(String email, String password) {

        // Tydzień 9 STARY KOD, dodaj zwracanie wyjątków zamiast kodów błędów
        /*
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        */

        // Tydzień 9 ZAD6.3 rzucenie celowego wyjątku HTTP 409 Conflict zamiast ogólnego RuntimeException (który powodował błąd 500)
        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public boolean checkPassword(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }

}
