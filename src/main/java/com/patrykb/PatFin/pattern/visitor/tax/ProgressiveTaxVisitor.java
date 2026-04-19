package com.patrykb.PatFin.pattern.visitor.tax;
public class ProgressiveTaxVisitor implements TaxVisitor { public void visit(IncomeTaxable i) { System.out.println("Progressive income tax calculated"); } public void visit(ExpenseTaxable e) { System.out.println("Checking deductible expenses"); } }
