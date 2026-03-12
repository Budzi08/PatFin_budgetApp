package com.patrykb.PatFin.pattern.bridge;

public class JsonFormatter implements ReportFormatter {
    @Override
    public String format(String title, String content) {
        return "{ \"title\": \"" + title + "\", \"content\": \"" + content + "\" }";
    }
}