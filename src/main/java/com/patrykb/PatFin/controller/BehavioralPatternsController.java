package com.patrykb.PatFin.controller;

import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.service.BehavioralPatternsService;
import com.patrykb.PatFin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/patterns/behavioral")
public class BehavioralPatternsController {

    @Autowired
    private BehavioralPatternsService behavioralPatternsService;

    @Autowired
    private UserService userService;

    @GetMapping("/run")
    public Map<String, Object> runAllBehavioralPatterns() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        // Uzycie wszystkich 5 nowych wzorcow behawioralnych
        return behavioralPatternsService.runAll(user);
    }
}
