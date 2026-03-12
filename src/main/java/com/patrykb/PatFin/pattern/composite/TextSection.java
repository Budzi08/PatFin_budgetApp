package com.patrykb.PatFin.pattern.composite;

public class TextSection implements ReportSection {
    private final String content;

    public TextSection(String content) {
        this.content = content;
    }

    @Override
    public String render() {
        return content + "\n";
    }
}