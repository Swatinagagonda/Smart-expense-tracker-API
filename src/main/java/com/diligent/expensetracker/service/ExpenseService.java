package com.diligent.expensetracker.service;

import com.diligent.expensetracker.dto.CreateExpenseRequest;
import com.diligent.expensetracker.model.Expense;

import java.math.BigDecimal;
import java.util.List;

/**
 * Defines the business operations for managing expenses.
 *
 * Implementations are responsible for applying business rules
 * (such as total calculation) on top of the underlying repository.
 */
public interface ExpenseService {

    Expense addExpense(CreateExpenseRequest request);

    List<Expense> getAllExpenses();

    List<Expense> getExpensesByCategory(String category);

    BigDecimal getTotalExpenses();

    BigDecimal getTotalExpensesByCategory(String category);

    void deleteExpense(Long id);
}
