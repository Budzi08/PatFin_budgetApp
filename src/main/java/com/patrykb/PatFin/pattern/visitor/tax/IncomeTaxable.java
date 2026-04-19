package com.patrykb.PatFin.pattern.visitor.tax;
public class IncomeTaxable implements TaxableElement { private double amount; public IncomeTaxable(double am) { amount = am; } public double getAmount() { return amount; } public void applyTax(TaxVisitor v) { v.visit(this); } }
