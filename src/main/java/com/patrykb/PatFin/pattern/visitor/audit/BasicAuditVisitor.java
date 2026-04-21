package com.patrykb.PatFin.pattern.visitor.audit;
public class BasicAuditVisitor implements AuditVisitor { public void visit(UserAudit user) { System.out.println("Audited User"); } public void visit(TransactionAudit transaction) { System.out.println("Audited Transaction"); } }
