package com.patrykb.PatFin.pattern.template.notification;
public class SmsWarningBuilder extends NotificationBuilderTemplate { protected void buildHeader() { System.out.println("SMS Header: Warning"); } protected void buildBody() { System.out.println("SMS Body: Please check account"); } }
