package com.patrykb.PatFin.pattern.decorator;

public class LoggingNotificationDecorator extends NotificationSenderDecorator {
    public LoggingNotificationDecorator(NotificationSender wrappedSender) {
        super(wrappedSender);
    }

    @Override
    public void send(String message) {
        System.out.println("[LOG] Preparing to send notification at " + java.time.LocalDateTime.now());
        wrappedSender.send(message);
        System.out.println("[LOG] Notification sent successfully.");
    }
}