package com.patrykb.PatFin.pattern.bridge;

public class DarkTheme implements ChartTheme {
    @Override
    public String applyTheme(String data) {
        return "[DARK THEME] " + data;
    }
}