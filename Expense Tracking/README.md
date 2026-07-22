# Expense Tracking Application

A full-stack expense tracker built from the supplied project document, using React.js for the frontend and Java Spring Boot for the backend.

## Features

- User registration, login, logout, and local demo forgot-password response
- Dashboard with total income, total expenses, current balance, monthly summary, and recent transactions
- Expense and income CRUD in one transaction screen
- Category CRUD with income/expense category types
- Search transactions by description or category
- Filter by date range, category, and transaction type
- Reports for monthly expenses, category-wise expenses, and income vs expense
- CSV export for transactions and reports
- Recurring monthly transaction flag
- Dark mode UI
- H2 file database with seeded demo data

## Tech Stack

- Frontend: React.js, Vite, CSS, lucide-react, Recharts
- Backend: Java 17, Spring Boot 3, Spring Web, Spring Data JPA, Bean Validation
- Database: H2 file database

## Run Backend

```powershell
cd backend
mvn spring-boot:run
```

Backend URL: `http://localhost:8080`

H2 console: `http://localhost:8080/h2-console`

H2 settings:

- JDBC URL: `jdbc:h2:file:./data/expense-tracker`
- User: `sa`
- Password: leave blank

## Run Frontend

Open a second terminal:

```powershell
cd frontend
npm install
npm run dev
```

Frontend URL: `http://localhost:5173`

## Demo Login

- Email: `demo@expense.local`
- Password: `password`

The backend creates this account and sample finance data automatically on first startup.

## API Summary

All protected endpoints require an `X-Auth-Token` header returned by login/register.

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/logout`
- `POST /api/auth/forgot-password`
- `GET /api/dashboard`
- `GET /api/categories`
- `POST /api/categories`
- `PUT /api/categories/{id}`
- `DELETE /api/categories/{id}`
- `GET /api/transactions`
- `POST /api/transactions`
- `PUT /api/transactions/{id}`
- `DELETE /api/transactions/{id}`
- `GET /api/reports`

## Project Structure

```text
backend/
  src/main/java/com/expensetracker/
    config/
    controller/
    dto/
    model/
    repository/
    service/
frontend/
  src/
    main.jsx
    styles.css
```

