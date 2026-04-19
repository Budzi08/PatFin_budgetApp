package com.patrykb.PatFin.pattern.state.account;
public class SuspendedAccountState implements AccountState { public void handleRequest() { System.out.println("Account is Suspended. Access denied."); } }
