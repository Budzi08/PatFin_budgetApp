package com.patrykb.PatFin.pattern.template.notification;
public abstract class NotificationBuilderTemplate { public final void buildAndSend() { buildHeader(); buildBody(); send(); } protected abstract void buildHeader(); protected abstract void buildBody(); protected void send() { System.out.println("Notification Dispatched"); } }
