package com.patrykb.PatFin.pattern.bridge;

public class EmailChannel implements AlertChannel {
    @Override
    public void send(String message) {
        System.out.println("Sending Email Alert: " + message);
    }
}