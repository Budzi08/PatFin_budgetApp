package com.patrykb.PatFin.pattern.visitor.audit;
public class DetailedAuditVisitor implements AuditVisitor { public void visit(UserAudit user) { System.out.println("Detailed Audit User: " + user.getName()); } public void visit(TransactionAudit t) { System.out.println("Detailed Audit Transaction: " + t.getAmount()); } }
