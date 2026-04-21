package com.patrykb.PatFin.pattern.state.transaction;
public class TransactionStateContext { private TransactionState state; public TransactionStateContext() { this.state = new PendingState(); } public void setState(TransactionState state) { this.state = state; } public void request() { state.handleState(this); } public TransactionState getState() { return state; } }
