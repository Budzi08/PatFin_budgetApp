package com.patrykb.PatFin.pattern.visitor.audit;
public class TransactionAudit implements AuditableElement { private double amount; public TransactionAudit(double amount) { this.amount = amount; } public double getAmount() { return amount; } public void accept(AuditVisitor v) { v.visit(this); } }
