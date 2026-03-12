package com.patrykb.PatFin.pattern.composite;

import java.util.ArrayList;
import java.util.List;

public class CompositeReportSection implements ReportSection {
    private final List<ReportSection> sections = new ArrayList<>();

    public void addSection(ReportSection section) {
        sections.add(section);
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder();
        for (ReportSection section : sections) {
            sb.append(section.render());
        }
        return sb.toString();
    }
}