package com.diligent.expensetracker.repository;

import com.diligent.expensetracker.model.Expense;

import java.util.List;

/**
 * Defines the in-memory data access contract for expenses.
 *
 * Implementations are responsible only for storing and retrieving
 * Expense data; no business logic (such as total calculation) belongs here.
 */
public interface ExpenseRepository {

    Expense save(Expense expense);

    List<Expense> findAll();

    List<Expense> findByCategory(String category);

    boolean deleteById(Long id);
}
