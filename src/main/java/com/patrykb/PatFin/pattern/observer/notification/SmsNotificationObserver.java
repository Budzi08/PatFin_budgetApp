package com.patrykb.PatFin.pattern.observer.notification;

public class SmsNotificationObserver implements NotificationObserver {
    @Override
    public void update(String message) { System.out.println("SMS sent: " + message); }
}
