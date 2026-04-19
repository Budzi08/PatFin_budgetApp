package com.patrykb.PatFin.pattern.template.dataimport;
public abstract class DataImportTemplate { public void processImport() { readData(); parseData(); saveData(); } protected abstract void readData(); protected abstract void parseData(); private void saveData() { System.out.println("Data saved to DB"); } }
