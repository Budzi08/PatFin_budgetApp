package com.patrykb.PatFin.pattern.adapter;

public interface ExternalAuthRequest {
    String getPrincipal();
    String getCredentials();
}