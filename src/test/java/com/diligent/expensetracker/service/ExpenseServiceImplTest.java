package com.diligent.expensetracker.service;

import com.diligent.expensetracker.dto.CreateExpenseRequest;
import com.diligent.expensetracker.exception.ExpenseNotFoundException;
import com.diligent.expensetracker.model.Expense;
import com.diligent.expensetracker.repository.ExpenseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Test
    void addExpense_savesAndReturnsExpenseFromRepository() {
        CreateExpenseRequest request = new CreateExpenseRequest(
                "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15));
        Expense savedExpense = new Expense(
                1L, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15));
        when(expenseRepository.save(any(Expense.class))).thenReturn(savedExpense);

        Expense result = expenseService.addExpense(request);

        assertThat(result).isEqualTo(savedExpense);
        verify(expenseRepository).save(any(Expense.class));
    }

    @Test
    void getAllExpenses_returnsAllExpensesFromRepository() {
        List<Expense> expenses = Arrays.asList(
                new Expense(1L, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15)),
                new Expense(2L, "Bus fare", new BigDecimal("10.00"), "Transport", LocalDate.of(2026, 1, 16)));
        when(expenseRepository.findAll()).thenReturn(expenses);

        List<Expense> result = expenseService.getAllExpenses();

        assertThat(result).isEqualTo(expenses);
    }

    @Test
    void getExpensesByCategory_returnsMatchingExpensesFromRepository() {
        List<Expense> foodExpenses = Collections.singletonList(
                new Expense(1L, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15)));
        when(expenseRepository.findByCategory("Food")).thenReturn(foodExpenses);

        List<Expense> result = expenseService.getExpensesByCategory("Food");

        assertThat(result).isEqualTo(foodExpenses);
    }

    @Test
    void getTotalExpenses_returnsSumOfAllExpenseAmounts() {
        List<Expense> expenses = Arrays.asList(
                new Expense(1L, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15)),
                new Expense(2L, "Bus fare", new BigDecimal("10.50"), "Transport", LocalDate.of(2026, 1, 16)));
        when(expenseRepository.findAll()).thenReturn(expenses);

        BigDecimal total = expenseService.getTotalExpenses();

        assertThat(total).isEqualByComparingTo(new BigDecimal("60.50"));
    }

    @Test
    void getTotalExpenses_returnsZeroWhenNoExpensesExist() {
        when(expenseRepository.findAll()).thenReturn(Collections.emptyList());

        BigDecimal total = expenseService.getTotalExpenses();

        assertThat(total).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void getTotalExpensesByCategory_returnsSumOfMatchingExpenseAmounts() {
        List<Expense> foodExpenses = Arrays.asList(
                new Expense(1L, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15)),
                new Expense(2L, "Snacks", new BigDecimal("15.25"), "Food", LocalDate.of(2026, 1, 17)));
        when(expenseRepository.findByCategory("Food")).thenReturn(foodExpenses);

        BigDecimal total = expenseService.getTotalExpensesByCategory("Food");

        assertThat(total).isEqualByComparingTo(new BigDecimal("65.25"));
    }

    @Test
    void getTotalExpensesByCategory_returnsZeroWhenNoExpensesMatchCategory() {
        when(expenseRepository.findByCategory("Unknown")).thenReturn(Collections.emptyList());

        BigDecimal total = expenseService.getTotalExpensesByCategory("Unknown");

        assertThat(total).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void deleteExpense_deletesWhenExpenseExists() {
        when(expenseRepository.deleteById(1L)).thenReturn(true);

        expenseService.deleteExpense(1L);

        verify(expenseRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteExpense_throwsExpenseNotFoundExceptionWhenExpenseDoesNotExist() {
        when(expenseRepository.deleteById(99L)).thenReturn(false);

        assertThatThrownBy(() -> expenseService.deleteExpense(99L))
                .isInstanceOf(ExpenseNotFoundException.class);
    }
}
