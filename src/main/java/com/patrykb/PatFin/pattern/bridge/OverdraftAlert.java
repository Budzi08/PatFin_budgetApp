package com.patrykb.PatFin.pattern.bridge;

public class OverdraftAlert extends Alert {
    public OverdraftAlert(AlertChannel channel) {
        super(channel);
    }

    @Override
    public void trigger(String issue) {
        channel.send("OVERDRAFT WARNING: " + issue);
    }
}