package com.patrykb.PatFin.pattern.memento;

import java.util.Stack;

// L5 Memento #3: Caretaker zarządzający historią stanów
public class ConfigHistory {
    private final Stack<ConfigMemento> history = new Stack<>();

    public void push(ConfigMemento memento) {
        history.push(memento);
    }

    public ConfigMemento pop() {
        if (!history.isEmpty()) {
            return history.pop();
        }
        return null;
    }
}