package com.patrykb.PatFin.pattern.decorator;

public class HtmlExportWriterDecorator extends ExportWriterDecorator {
    public HtmlExportWriterDecorator(ExportWriter wrappedWriter) {
        super(wrappedWriter);
    }

    @Override
    public String write(String data) {
        return "<html><body>" + wrappedWriter.write(data) + "</body></html>";
    }
}