package com.diligent.expensetracker.repository;

import com.diligent.expensetracker.model.Expense;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory implementation of ExpenseRepository.
 *
 * Stores expenses in a thread-safe map for the lifetime of the running
 * application. Data is not persisted and is lost on restart.
 */
@Repository
public class InMemoryExpenseRepository implements ExpenseRepository {

    private final Map<Long, Expense> expenses = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @Override
    public Expense save(Expense expense) {
        long generatedId = idSequence.incrementAndGet();
        expense.setId(generatedId);
        expenses.put(generatedId, expense);
        return expense;
    }

    @Override
    public List<Expense> findAll() {
        return new ArrayList<>(expenses.values());
    }

    @Override
    public List<Expense> findByCategory(String category) {
        return expenses.values().stream()
                .filter(expense -> expense.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(Long id) {
        return expenses.remove(id) != null;
    }
}
