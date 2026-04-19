package com.patrykb.PatFin.pattern.state.account;
public class AccountStateContext { private AccountState state; public void setState(AccountState state) { this.state = state; } public void applyState() { if (state != null) state.handleRequest(); } }
