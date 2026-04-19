package com.patrykb.PatFin.pattern.state.transaction;
public class PendingState implements TransactionState { public void handleState(TransactionStateContext context) { System.out.println("Transaction is Pending"); context.setState(new CompletedState()); } }
