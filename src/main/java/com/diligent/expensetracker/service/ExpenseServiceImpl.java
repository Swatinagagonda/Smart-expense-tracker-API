package com.diligent.expensetracker.service;

import com.diligent.expensetracker.dto.CreateExpenseRequest;
import com.diligent.expensetracker.exception.ExpenseNotFoundException;
import com.diligent.expensetracker.model.Expense;
import com.diligent.expensetracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implements the business operations defined by ExpenseService.
 *
 * Delegates all data storage and retrieval to ExpenseRepository,
 * and is responsible for business rules such as total calculation
 * and translating a failed delete into a domain exception.
 */
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense addExpense(CreateExpenseRequest request) {
        Expense expense = new Expense(
                null,
                request.getTitle(),
                request.getAmount(),
                request.getCategory(),
                request.getDate()
        );
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> getExpensesByCategory(String category) {
        return expenseRepository.findByCategory(category);
    }

    @Override
    public BigDecimal getTotalExpenses() {
        return expenseRepository.findAll().stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalExpensesByCategory(String category) {
        return expenseRepository.findByCategory(category).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void deleteExpense(Long id) {
        boolean deleted = expenseRepository.deleteById(id);
        if (!deleted) {
            throw new ExpenseNotFoundException(id);
        }
    }
}
