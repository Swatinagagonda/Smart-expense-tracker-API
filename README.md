# Smart Expense Tracker API

A REST API for managing personal expenses, built for the Diligent Software Engineering Apprenticeship 2026 take-home assignment.

## Project Overview

This API allows a client to:
- Add an expense (title, amount, category, date)
- View all expenses
- Filter expenses by category
- Calculate total expenses (overall and by category)
- Delete an expense

All data is stored **in memory only** — nothing is persisted to a database or file. Data is lost when the application restarts.

## Prerequisites

- **Java 17** or later
- **Maven 3.6+** (or use the included Maven Wrapper, if present)

Verify your setup:
```
java -version
mvn -version
```

## Build Instructions

From the project root (where `pom.xml` is located), run:
```
mvn clean install
```
This compiles the source code, runs the test suite, and packages the application into an executable JAR under `target/`.

## Run Instructions

Start the server with:
```
mvn spring-boot:run
```
The API will be available at:
```
http://localhost:8080
```

## Test Instructions

Run the full test suite with:
```
mvn test
```

**Note on test location:** The authoritative test suite lives under the Maven-standard path `src/test/java`, since that is the only location Maven's `mvn test` command will discover and run. A mirrored copy of the same test files is also included at the top-level `tests/` folder to match the repository structure shown in the assignment instructions; it is not compiled or run independently and exists only for structural visibility.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/expenses` | Add a new expense |
| `GET` | `/expenses` | View all expenses |
| `GET` | `/expenses?category={category}` | Filter expenses by category |
| `GET` | `/expenses/total` | Get the total of all expenses |
| `GET` | `/expenses/total?category={category}` | Get the total of expenses in a specific category |
| `DELETE` | `/expenses/{id}` | Delete an expense by id |

### Request Body — `POST /expenses`
```json
{
  "title": "Groceries",
  "amount": 50.00,
  "category": "Food",
  "date": "2026-01-15"
}
```

### Response — `POST /expenses` (201 Created)
```json
{
  "id": 1,
  "title": "Groceries",
  "amount": 50.00,
  "category": "Food",
  "date": "2026-01-15"
}
```

### Response — `DELETE /expenses/{id}` on a non-existent id (404 Not Found)
```json
{
  "status": 404,
  "message": "Expense not found with id: 5"
}
```

## Project Structure

```
expense-tracker/
├── README.md
├── AI_NOTES.md
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/com/diligent/expensetracker/
│   │   │   ├── ExpenseTrackerApplication.java
│   │   │   ├── controller/ExpenseController.java
│   │   │   ├── service/
│   │   │   │   ├── ExpenseService.java
│   │   │   │   └── ExpenseServiceImpl.java
│   │   │   ├── repository/
│   │   │   │   ├── ExpenseRepository.java
│   │   │   │   └── InMemoryExpenseRepository.java
│   │   │   ├── model/Expense.java
│   │   │   ├── dto/
│   │   │   │   ├── CreateExpenseRequest.java
│   │   │   │   └── ErrorResponse.java
│   │   │   └── exception/
│   │   │       ├── ExpenseNotFoundException.java
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/application.properties
│   └── test/
│       └── java/com/diligent/expensetracker/
│           ├── controller/ExpenseControllerTest.java
│           ├── service/ExpenseServiceImplTest.java
│           └── repository/InMemoryExpenseRepositoryTest.java
```

## Assumptions Made

Since the assignment left some details unspecified, the following assumptions were made:

- **Expense id**: Server-generated, using an incremental integer sequence starting at 1. Clients cannot supply their own id.
- **Amount type**: `BigDecimal`, to avoid floating-point rounding errors when representing money.
- **Date type and format**: `LocalDate`, accepted and returned in ISO-8601 format (`yyyy-MM-dd`).
- **Category type**: A free-text `String`, not restricted to a fixed set of values, since the assignment does not define one.
- **Filtering by a category with no matches**: Returns `200 OK` with an empty list, rather than an error.
- **Deleting a non-existent expense**: Returns `404 Not Found` with a JSON error body containing `status` and `message`.
- **Build tool**: Maven, since the assignment allows any stack and does not specify a build tool.
- **Java version**: 17 (current LTS), since the assignment does not specify a version.
- **No optional bonus feature was implemented**, per instruction to complete all mandatory requirements first and treat bonus features as optional.
- **No authentication, database, or frontend** was added, as none of these are part of the assignment's required features.
