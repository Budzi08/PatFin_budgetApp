package com.patrykb.PatFin.pattern.observer.status;

public class LoggingStatusObserver implements StatusObserver {
    @Override
    public void onStatusChanged(String status) { System.out.println("Log: System status changed to " + status); }
}
