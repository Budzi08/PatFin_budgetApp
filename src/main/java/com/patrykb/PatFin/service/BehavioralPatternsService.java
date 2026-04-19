package com.patrykb.PatFin.service;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.enums.TransactionType;

import com.patrykb.PatFin.pattern.observer.AuditObserver;
import com.patrykb.PatFin.pattern.observer.CategoryObserver;
import com.patrykb.PatFin.pattern.observer.ThresholdObserver;
import com.patrykb.PatFin.pattern.observer.TransactionEventPublisher;
import com.patrykb.PatFin.pattern.observer.notification.*;
import com.patrykb.PatFin.pattern.observer.status.*;

import com.patrykb.PatFin.pattern.state.BudgetLifecycleContext;
import com.patrykb.PatFin.pattern.state.transaction.*;
import com.patrykb.PatFin.pattern.state.account.*;

import com.patrykb.PatFin.pattern.strategy.ExpenseRatioStrategy;
import com.patrykb.PatFin.pattern.strategy.MetricContext;
import com.patrykb.PatFin.pattern.strategy.NetBalanceStrategy;
import com.patrykb.PatFin.pattern.strategy.SavingsRateStrategy;
import com.patrykb.PatFin.pattern.strategy.export.*;
import com.patrykb.PatFin.pattern.strategy.sort.*;

import com.patrykb.PatFin.pattern.template.CategoryReportTemplate;
import com.patrykb.PatFin.pattern.template.MonthlyReportTemplate;
import com.patrykb.PatFin.pattern.template.SummaryReportTemplate;
import com.patrykb.PatFin.pattern.template.dataimport.*;
import com.patrykb.PatFin.pattern.template.notification.*;

import com.patrykb.PatFin.pattern.visitor.BalanceElement;
import com.patrykb.PatFin.pattern.visitor.CsvMetricsVisitor;
import com.patrykb.PatFin.pattern.visitor.ExpenseElement;
import com.patrykb.PatFin.pattern.visitor.HumanReadableVisitor;
import com.patrykb.PatFin.pattern.visitor.IncomeElement;
import com.patrykb.PatFin.pattern.visitor.JsonMetricsVisitor;
import com.patrykb.PatFin.pattern.visitor.MetricElement;
import com.patrykb.PatFin.pattern.visitor.MetricsVisitor;
import com.patrykb.PatFin.pattern.visitor.audit.*;
import com.patrykb.PatFin.pattern.visitor.tax.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

@Service
public class BehavioralPatternsService {

    @Autowired
    private TransactionService transactionService;

    public Map<String, Object> runAll(User user) {
        List<Transaction> userTransactions = transactionService.findAllByUser(user);
        Transaction sample = userTransactions.isEmpty() ? buildSampleTransaction(user) : userTransactions.get(0);

        Map<String, Object> result = new LinkedHashMap<>();

        result.put("observer", runObserverDemo(sample));
        result.put("state", runStateDemo(sample));
        result.put("strategy", runStrategyDemo(userTransactions));
        result.put("template", runTemplateDemo(user, userTransactions));
        result.put("visitor", runVisitorDemo(userTransactions));

        return result;
    }

    private Transaction buildSampleTransaction(User user) {
        return Transaction.builder()
                .amount(new BigDecimal("120.00"))
                .description("Sample transaction for patterns")
                .date(LocalDate.now())
                .type(TransactionType.EXPENSE)
                .user(user)
                .build();
    }

    private Map<String, Object> runObserverDemo(Transaction transaction) {
        Map<String, Object> out = new LinkedHashMap<>();

        // USAGE 1: Transaction Event (Existing)
        List<String> logs = new ArrayList<>();
        TransactionEventPublisher publisher = new TransactionEventPublisher();
        publisher.subscribe(new AuditObserver());
        publisher.subscribe(new ThresholdObserver(new BigDecimal("500")));
        publisher.subscribe(new CategoryObserver());
        publisher.publish(transaction, logs);
        out.put("usage_1_transaction", logs);

        // USAGE 2: Notifications
        NotificationSubject ns = new NotificationSubject();
        ns.addObserver(new EmailNotificationObserver());
        ns.addObserver(new SmsNotificationObserver());
        ns.notifyObservers("Alert: System update");
        out.put("usage_2_notifications", "Fired to Email and SMS (see console)");

        // USAGE 3: System Status
        SystemStatusSubject ss = new SystemStatusSubject();
        ss.attach(new LoggingStatusObserver());
        ss.attach(new DashboardStatusObserver());
        ss.setStatus("MAINTENANCE");
        out.put("usage_3_status", "Status changed to MAINTENANCE");

        return out;
    }

    private Map<String, Object> runStateDemo(Transaction transaction) {
        Map<String, Object> out = new LinkedHashMap<>();

        // USAGE 1: Budget Lifecycle (Existing)
        BudgetLifecycleContext context = new BudgetLifecycleContext();
        List<String> flow = new ArrayList<>();
        flow.add(context.handle(transaction));
        context.nextState();
        flow.add(context.handle(transaction));
        out.put("usage_1_budget_lifecycle", flow);

        // USAGE 2: Transaction State
        TransactionStateContext txState = new TransactionStateContext();
        txState.request(); // Pending
        txState.request(); // Completed
        out.put("usage_2_transaction_state", "Transitioned Pending -> Completed");

        // USAGE 3: Account State
        AccountStateContext accState = new AccountStateContext();
        accState.setState(new ActiveAccountState());
        accState.applyState();
        accState.setState(new SuspendedAccountState());
        accState.applyState();
        out.put("usage_3_account_state", "Checked Active and Suspended states");

        return out;
    }

    private Map<String, Object> runStrategyDemo(List<Transaction> transactions) {
        Map<String, Object> out = new LinkedHashMap<>();

        // USAGE 1: Metrics Strategy (Existing)
        BigDecimal income = sumByType(transactions, TransactionType.INCOME);
        BigDecimal expense = sumByType(transactions, TransactionType.EXPENSE);
        MetricContext context = new MetricContext();
        Map<String, BigDecimal> metrics = new LinkedHashMap<>();
        context.setStrategy(new NetBalanceStrategy());
        metrics.put("NetBalance", context.calculate(income, expense));
        out.put("usage_1_metrics", metrics);

        // USAGE 2: Export Strategy
        ExportContext exportCtx = new ExportContext(new JsonExportStrategy());
        String expectedJson = exportCtx.executeExport("data123");
        exportCtx.setStrategy(new CsvExportStrategy());
        String expectedCsv = exportCtx.executeExport("data123");
        out.put("usage_2_export", "Exported JSON and CSV strategies");

        // USAGE 3: Sort Strategy
        SortContext sortCtx = new SortContext();
        List<String> items = Arrays.asList("Z", "A", "C");
        sortCtx.setStrategy(new AscendingSortStrategy());
        sortCtx.executeSort(items);
        sortCtx.setStrategy(new DescendingSortStrategy());
        sortCtx.executeSort(items);
        out.put("usage_3_sort", "Sorted Ascent/Descent");

        return out;
    }

    private Map<String, Object> runTemplateDemo(User user, List<Transaction> transactions) {
        Map<String, Object> out = new LinkedHashMap<>();

        // USAGE 1: Report Generation (Existing)
        out.put("usage_1_reports_summary", new SummaryReportTemplate().generate(user, transactions));

        // USAGE 2: Data Import
        DataImportTemplate csvImport = new CsvImportProcessor();
        csvImport.processImport();
        out.put("usage_2_data_import", "CSV Import Pipeline Executed");

        // USAGE 3: Notification Building
        NotificationBuilderTemplate emailBuilder = new EmailAlertBuilder();
        emailBuilder.buildAndSend();
        out.put("usage_3_notification_builder", "Email Notification Pipeline Executed");

        return out;
    }

    private Map<String, Object> runVisitorDemo(List<Transaction> transactions) {
        Map<String, Object> out = new LinkedHashMap<>();

        // USAGE 1: Metrics Visitor (Existing)
        BigDecimal income = sumByType(transactions, TransactionType.INCOME);
        BigDecimal expense = sumByType(transactions, TransactionType.EXPENSE);
        List<MetricElement> elements = List.of(new IncomeElement(income), new ExpenseElement(expense));
        MetricsVisitor jsonVisitor = new JsonMetricsVisitor();
        elements.forEach(e -> e.accept(jsonVisitor));
        out.put("usage_1_metrics_visitor", jsonVisitor.result());

        // USAGE 2: Audit Visitor
        AuditVisitor auditVisitor = new BasicAuditVisitor();
        new UserAudit("testUser").accept(auditVisitor);
        new TransactionAudit(100.0).accept(auditVisitor);
        out.put("usage_2_audit", "Basic Audit Executed");

        // USAGE 3: Tax Visitor
        TaxVisitor taxVisitor = new FlatTaxVisitor();
        new IncomeTaxable(1000).applyTax(taxVisitor);
        new ExpenseTaxable(200).applyTax(taxVisitor);
        out.put("usage_3_tax", "Flat Tax Calculation Executed");

        return out;
    }

    private BigDecimal sumByType(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
