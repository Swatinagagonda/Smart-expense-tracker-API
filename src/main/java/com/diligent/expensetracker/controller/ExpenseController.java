package com.diligent.expensetracker.controller;

import com.diligent.expensetracker.dto.CreateExpenseRequest;
import com.diligent.expensetracker.model.Expense;
import com.diligent.expensetracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * Exposes REST endpoints for managing personal expenses.
 *
 * Delegates all business logic to ExpenseService; this class is
 * responsible only for HTTP request and response handling.
 */
@RestController
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public Expense addExpense(@RequestBody CreateExpenseRequest request) {
        return expenseService.addExpense(request);
    }

    @GetMapping("/expenses")
    public List<Expense> getExpenses(@RequestParam(required = false) String category) {
        if (category != null) {
            return expenseService.getExpensesByCategory(category);
        }
        return expenseService.getAllExpenses();
    }

    @GetMapping("/expenses/total")
    public BigDecimal getTotalExpenses(@RequestParam(required = false) String category) {
        if (category != null) {
            return expenseService.getTotalExpensesByCategory(category);
        }
        return expenseService.getTotalExpenses();
    }

    @DeleteMapping("/expenses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }
}
