package com.patrykb.PatFin.pattern.command;

import com.patrykb.PatFin.dto.TransactionDto;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.pattern.facade.FinancialOperationFacade;

// L5 Command #2 Rejestracja transakcji
public class RegisterTransactionCommand implements FinancialCommand {
    private final FinancialOperationFacade facade;
    private final TransactionDto dto;
    private final User user;
    private Transaction result;

    public RegisterTransactionCommand(FinancialOperationFacade facade, TransactionDto dto, User user) {
        this.facade = facade;
        this.dto = dto;
        this.user = user;
    }

    @Override
    public void execute() {
        this.result = facade.registerTransaction(dto, user);
        System.out.println("L5 Command #2: Zarejestrowano transakcję przez obiekt Command");
    }

    public Transaction getResult() {
        return result;
    }
}