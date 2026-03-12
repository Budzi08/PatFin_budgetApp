package com.patrykb.PatFin.pattern.bridge;

public abstract class Alert {
    protected AlertChannel channel;

    protected Alert(AlertChannel channel) {
        this.channel = channel;
    }

    public abstract void trigger(String issue);
}