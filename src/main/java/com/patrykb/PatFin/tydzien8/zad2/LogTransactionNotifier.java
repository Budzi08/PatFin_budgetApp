package com.patrykb.PatFin.tydzien8.zad2;
import com.patrykb.PatFin.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class LogTransactionNotifier extends AbstractTransactionNotifier {
    @Override
    public void notifyUser(Transaction transaction) {
        String msg = buildAlertMessage(transaction);
        System.out.println("[Transaction Notifier LOG] " + msg);
    }
}