package com.diligent.expensetracker.exception;

/**
 * Thrown when an expense with the given id does not exist.
 */
public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(Long id) {
        super("Expense not found with id: " + id);
    }
}
