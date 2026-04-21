package com.patrykb.PatFin.pattern.visitor.audit;
public interface AuditableElement { void accept(AuditVisitor visitor); }
