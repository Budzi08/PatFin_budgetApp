package com.patrykb.PatFin.pattern.visitor.tax;
public class ExpenseTaxable implements TaxableElement { private double amount; public ExpenseTaxable(double am) { amount = am; } public double getAmount() { return amount; } public void applyTax(TaxVisitor v) { v.visit(this); } }
