package com.patrykb.PatFin.controller;

import com.patrykb.PatFin.security.JwtUtil;
import com.patrykb.PatFin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.dto.RegisterRequest;
import com.patrykb.PatFin.config.AuditLogger;

import com.patrykb.PatFin.pattern.adapter.ExternalAuthRequest;
import com.patrykb.PatFin.pattern.adapter.RegisterRequestAuthAdapter;
import com.patrykb.PatFin.pattern.decorator.NotificationSender;
import com.patrykb.PatFin.pattern.facade.UserOnboardingFacade;
import com.patrykb.PatFin.pattern.decorator.BasicNotificationSender;
import com.patrykb.PatFin.pattern.decorator.LoggingNotificationDecorator;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private UserOnboardingFacade onboardingFacade;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    static class ResponseFactory {
        static JwtResponse jwt(String token) {
            return new JwtResponse(token);
        }

        static RegisterResponse success(String message) {
            return new RegisterResponse(message);
        }

        static ErrorResponse error(String error) {
            return new ErrorResponse(error);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            // WZORZEC: Adapter (Use 3) - adaptacja Requestu do zewnętrznego interfejsu
            ExternalAuthRequest externalReq = new RegisterRequestAuthAdapter(request);
            System.out.println("External Auth Adapter - Principal: " + externalReq.getPrincipal());


            //userService.registerUser(request.getEmail(), request.getPassword());
            //AuditLogger.INSTANCE.logAuth(request.getEmail(), "REGISTER");
            //Facade - rejestracja użytkownika wraz z domyślnymi kategoriami i logowaniem zdarzenia w jednym miejscu
            onboardingFacade.onboardNewUser(request.getEmail(), request.getPassword());

            // WZORZEC: Decorator (Use 3) - dynamiczne dodanie logowania do wysyłki powiadomień
            NotificationSender sender = new LoggingNotificationDecorator(new BasicNotificationSender());
            sender.send("Witaj " + request.getEmail() + " w systemie PatFin!");

            return ResponseEntity.ok(ResponseFactory.success("Rejestracja zakończona sukcesem"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ResponseFactory.error(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user == null || !userService.checkPassword(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(ResponseFactory.error("Invalid email or password"));
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.isAdmin());
        AuditLogger.INSTANCE.logAuth(user.getEmail(), "LOGIN");
        return ResponseEntity.ok(ResponseFactory.jwt(token));
    }

    static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
    }

    static class RegisterResponse {
        private String message;

        public RegisterResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
