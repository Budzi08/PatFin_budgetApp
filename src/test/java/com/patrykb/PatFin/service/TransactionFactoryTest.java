package com.patrykb.PatFin.service;

import com.patrykb.PatFin.dto.TransactionDto;
import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.model.Transaction;
import com.patrykb.PatFin.model.User;
import com.patrykb.PatFin.model.enums.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class TransactionFactoryTest {

    private TransactionFactory transactionFactory;
    private User testUser;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        transactionFactory = new TransactionFactory();
        testUser = new User();
        testUser.setId(1L);
        testCategory = new Category();
        testCategory.setId(10L);
    }
    // Klasa 3 test 1
    // Testuje Tworzenie transakcji przychodowej.
    // Weryfikuje, czy przekazanie typu INCOME w DTO skutkuje utworzeniem obiektu Transaction z odpowiednim typem.
    @Test
    void shouldCreateIncomeTransaction() {
        TransactionDto dto = new TransactionDto();
        dto.setType(TransactionType.INCOME);
        dto.setAmount(new BigDecimal("100.00"));

        Transaction result = transactionFactory.create(dto, testCategory, testUser);
        assertEquals(TransactionType.INCOME, result.getType());
    }
    // Klasa 3 test 2
    // Testuje Tworzenie transakcji wydatkowej.
    // Weryfikuje, czy przekazanie typu EXPENSE w DTO skutkuje utworzeniem obiektu Transaction z odpowiednim typem.
    @Test
    void shouldCreateExpenseTransaction() {
        TransactionDto dto = new TransactionDto();
        dto.setType(TransactionType.EXPENSE);
        dto.setAmount(new BigDecimal("50.00"));

        Transaction result = transactionFactory.create(dto, testCategory, testUser);
        assertEquals(TransactionType.EXPENSE, result.getType());
    }
    // Klasa 3 test 3
    // Testuje Konwersję ujemnych kwot na wartości bezwzględne.
    // Weryfikuje, czy jeśli użytkownik przekaże ujemną kwotę w DTO (np. -50), fabryka zapisze ją jako dodatnią (50), chroniąc spójność bazy.
    @Test
    void shouldAlwaysSaveAbsoluteAmount() {
        TransactionDto dto = new TransactionDto();
        dto.setType(TransactionType.EXPENSE);
        dto.setAmount(new BigDecimal("-75.50"));

        Transaction result = transactionFactory.create(dto, testCategory, testUser);
        assertEquals(new BigDecimal("75.50"), result.getAmount());
    }
    // Klasa 3 test 4
    // Testuje Poprawne mapowanie opisu i daty.
    // Sprawdza, czy pola tekstowe i czasowe z DTO są dokładnie przepisane do docelowej encji.
    @Test
    void shouldMapDescriptionAndDateCorrectly() {
        LocalDate today = LocalDate.now();
        TransactionDto dto = new TransactionDto();
        dto.setType(TransactionType.INCOME);
        dto.setAmount(BigDecimal.TEN);
        dto.setDescription("Wypłata");
        dto.setDate(today);

        Transaction result = transactionFactory.create(dto, testCategory, testUser);
        assertEquals("Wypłata", result.getDescription());
        assertEquals(today, result.getDate());
    }
    // Klasa 3 test 5
    // Testuje: Przypisywanie relacji (Użytkownik i Kategoria).
    // Działanie: Weryfikuje, czy obiekty User i Category wstrzyknięte do metody create są poprawnie podpięte pod nową transakcję.
    @Test
    void shouldAssignCategoryAndUser() {
        TransactionDto dto = new TransactionDto();
        dto.setType(TransactionType.INCOME);
        dto.setAmount(BigDecimal.ONE);

        Transaction result = transactionFactory.create(dto, testCategory, testUser);
        assertEquals(testUser, result.getUser());
        assertEquals(testCategory, result.getCategory());
    }
}