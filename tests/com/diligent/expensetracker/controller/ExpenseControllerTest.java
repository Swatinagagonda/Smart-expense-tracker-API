package com.diligent.expensetracker.controller;

import com.diligent.expensetracker.dto.CreateExpenseRequest;
import com.diligent.expensetracker.exception.ExpenseNotFoundException;
import com.diligent.expensetracker.model.Expense;
import com.diligent.expensetracker.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExpenseService expenseService;

    @Test
    void addExpense_returns201AndCreatedExpense() throws Exception {
        CreateExpenseRequest request = new CreateExpenseRequest(
                "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15));
        Expense savedExpense = new Expense(
                1L, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15));
        when(expenseService.addExpense(any(CreateExpenseRequest.class))).thenReturn(savedExpense);

        mockMvc.perform(post("/expenses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Groceries"))
                .andExpect(jsonPath("$.category").value("Food"));
    }

    @Test
    void getExpenses_withoutCategory_returns200AndAllExpenses() throws Exception {
        List<Expense> expenses = Arrays.asList(
                new Expense(1L, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15)),
                new Expense(2L, "Bus fare", new BigDecimal("10.00"), "Transport", LocalDate.of(2026, 1, 16)));
        when(expenseService.getAllExpenses()).thenReturn(expenses);

        mockMvc.perform(get("/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getExpenses_withCategory_returns200AndFilteredExpenses() throws Exception {
        List<Expense> foodExpenses = Collections.singletonList(
                new Expense(1L, "Groceries", new BigDecimal("50.00"), "Food", LocalDate.of(2026, 1, 15)));
        when(expenseService.getExpensesByCategory("Food")).thenReturn(foodExpenses);

        mockMvc.perform(get("/expenses").param("category", "Food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].category").value("Food"));
    }

    @Test
    void getExpenses_withUnmatchedCategory_returns200AndEmptyList() throws Exception {
        when(expenseService.getExpensesByCategory("Unknown")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/expenses").param("category", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getTotalExpenses_withoutCategory_returns200AndOverallTotal() throws Exception {
        when(expenseService.getTotalExpenses()).thenReturn(new BigDecimal("60.50"));

        mockMvc.perform(get("/expenses/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(60.50));
    }

    @Test
    void getTotalExpenses_withCategory_returns200AndCategoryTotal() throws Exception {
        when(expenseService.getTotalExpensesByCategory(eq("Food"))).thenReturn(new BigDecimal("65.25"));

        mockMvc.perform(get("/expenses/total").param("category", "Food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(65.25));
    }

    @Test
    void deleteExpense_whenExpenseExists_returns204() throws Exception {
        doNothing().when(expenseService).deleteExpense(1L);

        mockMvc.perform(delete("/expenses/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteExpense_whenExpenseDoesNotExist_returns404() throws Exception {
        doThrow(new ExpenseNotFoundException(99L)).when(expenseService).deleteExpense(99L);

        mockMvc.perform(delete("/expenses/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
