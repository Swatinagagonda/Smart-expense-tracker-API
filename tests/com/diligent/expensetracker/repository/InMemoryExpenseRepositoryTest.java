package com.diligent.expensetracker.repository;

import com.diligent.expensetracker.model.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryExpenseRepositoryTest {

    private InMemoryExpenseRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryExpenseRepository();
    }

    @Test
    void save_assignsSequentialIdsStartingFromOne() {
        Expense first = new Expense(null, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15));
        Expense second = new Expense(null, "Bus fare", new BigDecimal("10.00"), "Transport", LocalDate.of(2026, 1, 16));

        Expense savedFirst = repository.save(first);
        Expense savedSecond = repository.save(second);

        assertThat(savedFirst.getId()).isEqualTo(1L);
        assertThat(savedSecond.getId()).isEqualTo(2L);
    }

    @Test
    void findAll_returnsAllSavedExpenses() {
        repository.save(new Expense(null, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15)));
        repository.save(new Expense(null, "Bus fare", new BigDecimal("10.00"), "Transport", LocalDate.of(2026, 1, 16)));

        List<Expense> result = repository.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findAll_returnsEmptyListWhenNoExpensesSaved() {
        List<Expense> result = repository.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    void findByCategory_returnsOnlyMatchingExpenses() {
        repository.save(new Expense(null, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15)));
        repository.save(new Expense(null, "Snacks", new BigDecimal("15.25"), "Food", LocalDate.of(2026, 1, 17)));
        repository.save(new Expense(null, "Bus fare", new BigDecimal("10.00"), "Transport", LocalDate.of(2026, 1, 16)));

        List<Expense> result = repository.findByCategory("Food");

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(expense -> expense.getCategory().equals("Food"));
    }

    @Test
    void findByCategory_returnsEmptyListWhenNoMatches() {
        repository.save(new Expense(null, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15)));

        List<Expense> result = repository.findByCategory("Entertainment");

        assertThat(result).isEmpty();
    }

    @Test
    void deleteById_returnsTrueAndRemovesExpenseWhenItExists() {
        Expense saved = repository.save(new Expense(null, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15)));

        boolean deleted = repository.deleteById(saved.getId());

        assertThat(deleted).isTrue();
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void deleteById_returnsFalseWhenExpenseDoesNotExist() {
        boolean deleted = repository.deleteById(999L);

        assertThat(deleted).isFalse();
    }
}
