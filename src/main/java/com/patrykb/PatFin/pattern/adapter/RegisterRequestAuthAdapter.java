package com.patrykb.PatFin.pattern.adapter;

import com.patrykb.PatFin.dto.RegisterRequest;

public class RegisterRequestAuthAdapter implements ExternalAuthRequest {
    private final RegisterRequest request;

    public RegisterRequestAuthAdapter(RegisterRequest request) {
        this.request = request;
    }

    @Override
    public String getPrincipal() {
        return request.getEmail();
    }

    @Override
    public String getCredentials() {
        return request.getPassword();
    }
}