package com.patrykb.PatFin.pattern.command;

import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.pattern.proxy.SecurityTransactionProxy;

// L5 Command #1 Bezpieczne usuwanie
public class DeleteTransactionCommand implements FinancialCommand {
    private final SecurityTransactionProxy securityProxy;
    private final Long id;
    private final User user;

    public DeleteTransactionCommand(SecurityTransactionProxy proxy, Long id, User user) {
        this.securityProxy = proxy;
        this.id = id;
        this.user = user;
    }

    @Override
    public void execute() {
        securityProxy.safeDelete(id, user);
        System.out.println("L5 Command #1: Wykonano bezpieczne usunięcie przez obiekt Command dla ID: " + id);
    }
}