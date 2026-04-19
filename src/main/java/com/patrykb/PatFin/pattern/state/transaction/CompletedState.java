package com.patrykb.PatFin.pattern.state.transaction;
public class CompletedState implements TransactionState { public void handleState(TransactionStateContext context) { System.out.println("Transaction is Completed"); } }
