package com.patrykb.PatFin.controller;

import com.patrykb.PatFin.dto.TransactionDto;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.pattern.command.DeleteTransactionCommand;
import com.patrykb.PatFin.pattern.command.FinancialCommand;
import com.patrykb.PatFin.pattern.command.RegisterTransactionCommand;
import com.patrykb.PatFin.pattern.iterator.PatFinIterator;
import com.patrykb.PatFin.service.TransactionService;
import com.patrykb.PatFin.service.UserService;
import com.patrykb.PatFin.model.enums.TransactionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.springframework.security.core.Authentication;
import com.patrykb.PatFin.model.User;

import com.patrykb.PatFin.pattern.decorator.TransactionDataSource;
import com.patrykb.PatFin.pattern.facade.FinancialOperationFacade;
import com.patrykb.PatFin.pattern.proxy.SecurityTransactionProxy;
import com.patrykb.PatFin.pattern.decorator.DatabaseTransactionSource;
import com.patrykb.PatFin.pattern.decorator.CachedTransactionSource;
import com.patrykb.PatFin.pattern.bridge.Alert;
import com.patrykb.PatFin.pattern.bridge.OverdraftAlert;
import com.patrykb.PatFin.pattern.bridge.EmailChannel;
import com.patrykb.PatFin.pattern.adapter.ExportableItem;
import com.patrykb.PatFin.pattern.adapter.TransactionExportAdapter;
import com.patrykb.PatFin.pattern.decorator.ExportWriter;
import com.patrykb.PatFin.pattern.decorator.SimpleExportWriter;
import com.patrykb.PatFin.pattern.decorator.HtmlExportWriterDecorator;
import com.patrykb.PatFin.pattern.proxy.SecurityTransactionProxy;
import com.patrykb.PatFin.tydzien8.DescriptionFormatter;
import com.patrykb.PatFin.tydzien8.UpperCaseDescriptionFormatter;
import com.patrykb.PatFin.tydzien8.zad2.TransactionNotifier;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private SecurityTransactionProxy securityProxy;

    @Autowired
    private FinancialOperationFacade financialFacade;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Transaction> getUserTransactions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        // WZORZEC: Decorator (Use 1)
        TransactionDataSource source = new CachedTransactionSource(new DatabaseTransactionSource(transactionService));

        return transactionService.findAllByUser(user);
    }

    @Autowired
    private TransactionNotifier notifier;

    @PostMapping
    public Transaction addTransaction(@RequestBody TransactionDto dto, @RequestParam(required = false, defaultValue = "false") boolean UpperCaseFormat) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);

        DescriptionFormatter formatter; //zmienna typu bazowego
        if (UpperCaseFormat) {
            formatter = new UpperCaseDescriptionFormatter(); // Typ pochodny
        } else {
            formatter = new DescriptionFormatter();     // Typ bazowy
        }

        if (dto.getDescription() != null) {
            dto.setDescription(formatter.format(dto.getDescription()));
        }

//        //Transaction saved = transactionService.save(dto, user);
//        //WZORZEC Facade użycie 2 - rejestracja transakcji wraz z weryfikacją kategorii i automatycznym audytem
//        Transaction saved = financialFacade.registerTransaction(dto, user);
//
//        // WZORZEC: Bridge (Use 2) - Wysyłamy alert przekroczenia budżetu, jeśli wydatek > 1000 PLN
//        if (saved.getType() == TransactionType.EXPENSE && saved.getAmount().compareTo(new BigDecimal("1000")) > 0) {
//            Alert alert = new OverdraftAlert(new EmailChannel());
//            alert.trigger("Zarejestrowano bardzo wysoki wydatek: " + saved.getAmount());
//        }

//        return saved;

        // L5 Command #2
        RegisterTransactionCommand regCommand = new RegisterTransactionCommand(financialFacade, dto, user);
        regCommand.execute();

        Transaction savedTransaction = regCommand.getResult();

        // progi kwotowe i logika powiadomień są ukryte w implementacji
        if (notifier.isApplicable(savedTransaction)) {
            notifier.notifyUser(savedTransaction);
        }

        return savedTransaction;
    }
// Tydzień 9 STARY KOD, dostosuj 3 funkcje tak by spełniały tylko jedną rolę
//    @GetMapping("/export")
//    public String exportTransactions() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = (String) authentication.getPrincipal();
//        User user = userService.findByEmail(email);
//        List<Transaction> transactions = transactionService.findAllByUser(user);
//
//        StringBuilder sb = new StringBuilder();
////        for(Transaction t : transactions) {
////            // WZORZEC: Adapter (Use 1)
////            ExportableItem item = new TransactionExportAdapter(t);
////            sb.append("Tytul: ").append(item.getExportTitle())
////                    .append(" | Kwota: ").append(item.getExportValue()).append("<br/>");
////        }
//
//        // L5 Iterator #1
//        PatFinIterator<Transaction> it = new PatFinIterator<>() {
//            private int index = 0;
//            public boolean hasNext() { return index < transactions.size(); }
//            public Transaction next() { return transactions.get(index++); }
//        };
//
//        while (it.hasNext()) {
//            Transaction t = it.next();
//            ExportableItem item = new TransactionExportAdapter(t);
//            sb.append("Tytul: ").append(item.getExportTitle())
//                    .append(" | Kwota: ").append(item.getExportValue()).append("<br/>");
//        }
//
//        // WZORZEC: Decorator (Use 2) - Dekorowanie wyjścia formatem HTML
//        ExportWriter writer = new HtmlExportWriterDecorator(new SimpleExportWriter());
//        return writer.write(sb.toString());
//    }

// Tydzień 9 ZAD3.2 dostosuj 3 funkcje tak by spełniały tylko jedną rolę
@GetMapping("/export")
public String exportTransactions() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = (String) authentication.getPrincipal();
    User user = userService.findByEmail(email);
    List<Transaction> transactions = transactionService.findAllByUser(user);

    return generateHtmlExport(transactions);
}

    // Tydzień 9 ZAD3.2 dostosuj 3 funkcje tak by spełniały tylko jedną rolę - funkcja wydzielona dla SRP
    private String generateHtmlExport(List<Transaction> transactions) {
        StringBuilder sb = new StringBuilder();
//        for(Transaction t : transactions) {
//            // WZORZEC: Adapter (Use 1)
//            ExportableItem item = new TransactionExportAdapter(t);
//            sb.append("Tytul: ").append(item.getExportTitle())
//                    .append(" | Kwota: ").append(item.getExportValue()).append("<br/>");
//        }

        // L5 Iterator #1
        PatFinIterator<Transaction> it = new PatFinIterator<>() {
            private int index = 0;
            public boolean hasNext() { return index < transactions.size(); }
            public Transaction next() { return transactions.get(index++); }
        };

        while (it.hasNext()) {
            Transaction t = it.next();
            ExportableItem item = new TransactionExportAdapter(t);
            sb.append("Tytul: ").append(item.getExportTitle())
                    .append(" | Kwota: ").append(item.getExportValue()).append("<br/>");
        }

        // WZORZEC: Decorator (Use 2) - Dekorowanie wyjścia formatem HTML
        ExportWriter writer = new HtmlExportWriterDecorator(new SimpleExportWriter());
        return writer.write(sb.toString());
    }

    // Tydzień 9 wyodrębniono powtarzający się kod do wspólnej funkcji pomocniczej
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        return userService.findByEmail(email);
    }

    @GetMapping("/filter")
    public List<Transaction> getTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) Long categoryId) {

        // Tydzień 9 STARY KOD, dostosuj kod tak, by się nie powtarzał
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        */
        // Tydzień 9 ZAD7 użyto wspólnej metody getCurrentUser(), eliminując powtórzenie
        User user = getCurrentUser();

        return transactionService.findAllByUserWithFilters(
                user, startDate, endDate, minAmount, maxAmount, type, categoryId);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        // Tydzień 9 STARY KOD, dostosuj kod tak, by się nie powtarzał. Z
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        */
        // Tydzień 9 ZAD7 użyto wspólnej metody getCurrentUser(), eliminując powtórzenie
        User user = getCurrentUser();

        // L5 Command #1
        FinancialCommand delCommand = new DeleteTransactionCommand(securityProxy, id, user);
        delCommand.execute();
    }

    @PostMapping("/{id}/duplicate")
    public Transaction duplicateTransaction(@PathVariable Long id) {
        // Tydzień 9 STARY KOD, dostosuj kod tak, by się nie powtarzał
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        User user = userService.findByEmail(email);
        */
        // Tydzień 9 ZAD7 użyto wspólnej metody getCurrentUser(), eliminując powtórzenie
        User user = getCurrentUser();

        return transactionService.duplicate(id, user);
    }

}
