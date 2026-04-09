package com.patrykb.PatFin.service;

import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.enums.TransactionType;
import com.patrykb.PatFin.pattern.observer.AuditObserver;
import com.patrykb.PatFin.pattern.observer.CategoryObserver;
import com.patrykb.PatFin.pattern.observer.ThresholdObserver;
import com.patrykb.PatFin.pattern.observer.TransactionEventPublisher;
import com.patrykb.PatFin.pattern.state.BudgetLifecycleContext;
import com.patrykb.PatFin.pattern.strategy.ExpenseRatioStrategy;
import com.patrykb.PatFin.pattern.strategy.MetricContext;
import com.patrykb.PatFin.pattern.strategy.NetBalanceStrategy;
import com.patrykb.PatFin.pattern.strategy.SavingsRateStrategy;
import com.patrykb.PatFin.pattern.template.CategoryReportTemplate;
import com.patrykb.PatFin.pattern.template.MonthlyReportTemplate;
import com.patrykb.PatFin.pattern.template.SummaryReportTemplate;
import com.patrykb.PatFin.pattern.visitor.BalanceElement;
import com.patrykb.PatFin.pattern.visitor.CsvMetricsVisitor;
import com.patrykb.PatFin.pattern.visitor.ExpenseElement;
import com.patrykb.PatFin.pattern.visitor.HumanReadableVisitor;
import com.patrykb.PatFin.pattern.visitor.IncomeElement;
import com.patrykb.PatFin.pattern.visitor.JsonMetricsVisitor;
import com.patrykb.PatFin.pattern.visitor.MetricElement;
import com.patrykb.PatFin.pattern.visitor.MetricsVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        List<String> logs = new ArrayList<>();
        TransactionEventPublisher publisher = new TransactionEventPublisher();

        // observer 1/3
        publisher.subscribe(new AuditObserver());
        // observer 2/3
        publisher.subscribe(new ThresholdObserver(new BigDecimal("500")));
        // observer 3/3
        publisher.subscribe(new CategoryObserver());

        // observer usage
        publisher.publish(transaction, logs);

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("transaction", transaction.getDescription());
        out.put("notifications", logs);
        return out;
    }

    private Map<String, Object> runStateDemo(Transaction transaction) {
        BudgetLifecycleContext context = new BudgetLifecycleContext();
        List<String> flow = new ArrayList<>();

        // state 1/3 usage
        flow.add(context.handle(transaction));
        context.nextState();
        // state 2/3 usage
        flow.add(context.handle(transaction));
        context.nextState();
        // state 3/3 usage
        flow.add(context.handle(transaction));

        Map<String, Object> out = new LinkedHashMap<>();
        out.put("flow", flow);
        out.put("finalState", context.currentStateName());
        return out;
    }

    private Map<String, Object> runStrategyDemo(List<Transaction> transactions) {
        BigDecimal income = sumByType(transactions, TransactionType.INCOME);
        BigDecimal expense = sumByType(transactions, TransactionType.EXPENSE);

        MetricContext context = new MetricContext();
        Map<String, BigDecimal> metrics = new LinkedHashMap<>();

        // strategy 1/3 usage
        context.setStrategy(new NetBalanceStrategy());
        metrics.put(context.getStrategyName(), context.calculate(income, expense));

        // strategy 2/3 usage
        context.setStrategy(new SavingsRateStrategy());
        metrics.put(context.getStrategyName(), context.calculate(income, expense));

        // strategy 3/3 usage
        context.setStrategy(new ExpenseRatioStrategy());
        metrics.put(context.getStrategyName(), context.calculate(income, expense));

        return new LinkedHashMap<>(metrics);
    }

    private Map<String, String> runTemplateDemo(User user, List<Transaction> transactions) {
        Map<String, String> reports = new LinkedHashMap<>();

        // template 1/3 usage
        reports.put("summary", new SummaryReportTemplate().generate(user, transactions));
        // template 2/3 usage
        reports.put("category", new CategoryReportTemplate().generate(user, transactions));
        // template 3/3 usage
        reports.put("monthly", new MonthlyReportTemplate().generate(user, transactions));

        return reports;
    }

    private Map<String, String> runVisitorDemo(List<Transaction> transactions) {
        BigDecimal income = sumByType(transactions, TransactionType.INCOME);
        BigDecimal expense = sumByType(transactions, TransactionType.EXPENSE);
        BigDecimal balance = income.subtract(expense);

        List<MetricElement> elements = List.of(
                new IncomeElement(income),
                new ExpenseElement(expense),
                new BalanceElement(balance)
        );

        Map<String, String> out = new LinkedHashMap<>();

        // visitor 1/3 usage
        MetricsVisitor jsonVisitor = new JsonMetricsVisitor();
        elements.forEach(e -> e.accept(jsonVisitor));
        out.put("json", jsonVisitor.result());

        // visitor 2/3 usage
        MetricsVisitor csvVisitor = new CsvMetricsVisitor();
        elements.forEach(e -> e.accept(csvVisitor));
        out.put("csv", csvVisitor.result());

        // visitor 3/3 usage
        MetricsVisitor humanVisitor = new HumanReadableVisitor();
        elements.forEach(e -> e.accept(humanVisitor));
        out.put("human", humanVisitor.result());

        return out;
    }

    private BigDecimal sumByType(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
