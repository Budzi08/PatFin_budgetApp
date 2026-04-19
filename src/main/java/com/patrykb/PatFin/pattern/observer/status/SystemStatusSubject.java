package com.patrykb.PatFin.pattern.observer.status;

import java.util.ArrayList;
import java.util.List;

public class SystemStatusSubject {
    private String currentStatus;
    private List<StatusObserver> observers = new ArrayList<>();
    public void attach(StatusObserver o) { observers.add(o); }
    public void setStatus(String status) { this.currentStatus = status; notifyAllObservers(); }
    private void notifyAllObservers() { for (StatusObserver o : observers) { o.onStatusChanged(currentStatus); } }
}
