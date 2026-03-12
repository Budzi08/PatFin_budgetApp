package com.patrykb.PatFin.pattern.decorator;

public abstract class NotificationSenderDecorator implements NotificationSender {
    protected NotificationSender wrappedSender;

    public NotificationSenderDecorator(NotificationSender wrappedSender) {
        this.wrappedSender = wrappedSender;
    }
}