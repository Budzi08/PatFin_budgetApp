package com.patrykb.PatFin.pattern.observer.status;

public class DashboardStatusObserver implements StatusObserver {
    @Override
    public void onStatusChanged(String status) { System.out.println("Dashboard string update: " + status); }
}
