package com.patrykb.PatFin.pattern.decorator;

public abstract class ExportWriterDecorator implements ExportWriter {
    protected ExportWriter wrappedWriter;

    public ExportWriterDecorator(ExportWriter wrappedWriter) {
        this.wrappedWriter = wrappedWriter;
    }
}