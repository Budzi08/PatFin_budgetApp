package com.patrykb.PatFin.pattern.visitor.tax;
public interface TaxVisitor { void visit(IncomeTaxable income); void visit(ExpenseTaxable expense); }
