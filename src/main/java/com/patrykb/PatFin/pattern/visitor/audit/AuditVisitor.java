package com.patrykb.PatFin.pattern.visitor.audit;
public interface AuditVisitor { void visit(UserAudit user); void visit(TransactionAudit transaction); }
