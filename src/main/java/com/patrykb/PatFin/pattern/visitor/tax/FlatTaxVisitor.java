package com.patrykb.PatFin.pattern.visitor.tax;
public class FlatTaxVisitor implements TaxVisitor { public void visit(IncomeTaxable income) { System.out.println("Flat tax for Income: " + (income.getAmount() * 0.19)); } public void visit(ExpenseTaxable exp) { System.out.println("No tax on expenses"); } }
