package com.patrykb.PatFin.pattern.visitor.audit;
public class UserAudit implements AuditableElement { private String name; public UserAudit(String name) { this.name = name; } public String getName() { return name; } public void accept(AuditVisitor v) { v.visit(this); } }
