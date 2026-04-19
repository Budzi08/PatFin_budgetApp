package com.patrykb.PatFin.pattern.state.account;
public class ActiveAccountState implements AccountState { public void handleRequest() { System.out.println("Account is Active. Access granted."); } }
