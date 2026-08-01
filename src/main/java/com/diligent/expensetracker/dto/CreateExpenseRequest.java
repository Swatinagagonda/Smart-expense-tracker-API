package com.diligent.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request payload for creating a new expense.
 *
 * Does not include an id, since the server generates it automatically.
 */
public class CreateExpenseRequest {

    private String title;
    private BigDecimal amount;
    private String category;
    private LocalDate date;

    public CreateExpenseRequest() {
    }

    public CreateExpenseRequest(String title, BigDecimal amount, String category, LocalDate date) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
