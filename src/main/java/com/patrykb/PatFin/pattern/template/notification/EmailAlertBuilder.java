package com.patrykb.PatFin.pattern.template.notification;
public class EmailAlertBuilder extends NotificationBuilderTemplate { protected void buildHeader() { System.out.println("Email Subject: Alert"); } protected void buildBody() { System.out.println("Email Body: Content"); } }
