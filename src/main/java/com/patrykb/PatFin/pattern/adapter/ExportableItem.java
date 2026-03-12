package com.patrykb.PatFin.pattern.adapter;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExportableItem {
    String getExportTitle();
    BigDecimal getExportValue();
    LocalDate getExportDate();
}