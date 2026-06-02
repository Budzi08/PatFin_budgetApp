package com.patrykb.PatFin.service;

import com.patrykb.PatFin.model.Category;
import com.patrykb.PatFin.repository.CategoryRepository;
import com.patrykb.PatFin.pattern.mediator.PatFinMediator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private PatFinMediator mediator;

    @InjectMocks
    private CategoryService categoryService;
    // Klasa 2 test 1
    // Testuje Pobieranie kategorii po ID.
    // Weryfikuje, czy dla istniejącego ID w bazie serwis poprawnie zwraca obiekt kategorii.
    @Test
    void shouldFindCategoryByIdWhenExists() {
        Category cat = new Category();
        cat.setId(1L);
        cat.setName("Jedzenie");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat));

        Category result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals("Jedzenie", result.getName());
    }
    // Klasa 2 test 2
    // Testuje Rzucanie błędu przy braku kategorii.
    // Sprawdza, czy próba pobrania nieistniejącej kategorii rzuca wyjątek HTTP 404 NOT FOUND (nasza modyfikacja Clean Code).
    @Test
    void shouldThrowNotFoundWhenCategoryDoesNotExist() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            categoryService.findById(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
    // Klasa 2 test 3
    // Testuje Pobieranie wszystkich kategorii.
    // Weryfikuje, czy wywołanie findAll prawidłowo deleguje żądanie do repozytorium i zwraca kompletną listę.
    @Test
    void shouldReturnAllCategories() {
        Category c1 = new Category();
        Category c2 = new Category();
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Category> results = categoryService.findAll();

        assertEquals(2, results.size());
        verify(categoryRepository, times(1)).findAll();
    }
    // Klasa 2 test 4
    // Testuje Wyszukiwanie kategorii po nazwie.
    // Sprawdza, czy serwis poprawnie wywołuje repozytorium podczas szukania według pola tekstowego np zapobieganie duplikatom
    @Test
    void shouldFindCategoryByName() {
        Category cat = new Category();
        cat.setName("Transport");
        when(categoryRepository.findByName("Transport")).thenReturn(Optional.of(cat));

        Optional<Category> result = categoryService.findByName("Transport");

        assertTrue(result.isPresent());
        assertEquals("Transport", result.get().getName());
    }
    // Klasa 2 test 5
    // Testuje Zapisywanie nowej kategorii.
    // Weryfikuje, że metoda save nie realizuje ukrytej logiki zmieniającej obiekt i przekazuje go prosto do repozytorium
    @Test
    void shouldSaveCategoryDirectlyToRepository() {
        Category catToSave = new Category();
        catToSave.setName("Rozrywka");
        when(categoryRepository.save(catToSave)).thenReturn(catToSave);

        Category result = categoryService.save(catToSave);

        assertEquals("Rozrywka", result.getName());

        verify(categoryRepository, times(1)).save(catToSave);

        verify(mediator, times(1)).notify(eq(categoryService), anyString());
    }
}