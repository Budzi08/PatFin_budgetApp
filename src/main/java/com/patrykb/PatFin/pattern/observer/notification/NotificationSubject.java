package com.patrykb.PatFin.pattern.observer.notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationSubject {
    private List<NotificationObserver> observers = new ArrayList<>();
    public void addObserver(NotificationObserver o) { observers.add(o); }
    public void notifyObservers(String msg) { for (NotificationObserver o : observers) { o.update(msg); } }
}
