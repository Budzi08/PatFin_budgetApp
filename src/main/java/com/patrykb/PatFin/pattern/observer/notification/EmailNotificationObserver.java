package com.patrykb.PatFin.pattern.observer.notification;

public class EmailNotificationObserver implements NotificationObserver {
    @Override
    public void update(String message) { System.out.println("Email sent: " + message); }
}
