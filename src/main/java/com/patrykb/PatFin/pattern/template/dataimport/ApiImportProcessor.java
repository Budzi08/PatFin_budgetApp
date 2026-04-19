package com.patrykb.PatFin.pattern.template.dataimport;
public class ApiImportProcessor extends DataImportTemplate { protected void readData() { System.out.println("Fetching data from API"); } protected void parseData() { System.out.println("Parsing JSON from API"); } }
