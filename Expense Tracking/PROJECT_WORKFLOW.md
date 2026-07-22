# Expense Tracking Application - Project Workflow

## 1. Project Overview

The Expense Tracking Application is a full-stack web application built with React.js and Java Spring Boot. It allows users to register, log in, manage income and expenses, organize transactions by category, view dashboard summaries, filter records, and generate reports.

## 2. High-Level Architecture Workflow

User -> React Frontend -> Spring Boot REST API -> Service Layer -> Repository Layer -> H2 Database

Response flow:

H2 Database -> Repository Layer -> Service Layer -> REST Controller -> React UI -> User

## 3. User Authentication Workflow

1. User opens the React application.
2. User chooses Login or Register.
3. React sends credentials to Spring Boot authentication API.
4. Backend validates registration fields or checks login password.
5. Backend returns an authentication token and user details.
6. React stores the session in browser local storage.
7. Protected API calls include the token in the X-Auth-Token header.
8. User can log out, which clears the token.

## 4. Dashboard Workflow

1. User logs in successfully.
2. React calls the dashboard API.
3. Backend loads the user's transactions.
4. Service layer calculates total income, total expenses, current balance, monthly income, monthly expenses, and recent transactions.
5. React displays cards and charts using the response data.

## 5. Category Management Workflow

1. User opens the Categories page.
2. React fetches all categories for the logged-in user.
3. User creates, edits, or deletes a category.
4. Backend validates category name and type.
5. Backend saves changes through JPA repositories.
6. React refreshes the category list.
7. Categories used by transactions are protected from deletion.

## 6. Transaction Management Workflow

1. User opens the Transactions page.
2. React fetches categories and transaction records.
3. User adds income or expense with amount, category, date, description, and recurring flag.
4. Backend validates amount, date, category ownership, and category type.
5. Backend stores the transaction in the database.
6. User can edit or delete existing transactions.
7. Dashboard and reports update after changes.

## 7. Search and Filter Workflow

1. User enters a search term or selects filters.
2. Filters can include date range, category, and transaction type.
3. React sends query parameters to the transactions API.
4. Backend applies the filters in the repository query.
5. React displays the matching transactions.

## 8. Reports and Analytics Workflow

1. User opens the Reports page.
2. User selects a date range.
3. React calls the reports API.
4. Backend groups transaction data into monthly expenses, category-wise expenses, and income vs expense totals.
5. React renders pie and bar charts.
6. User can export report data to CSV.

## 9. Database Workflow

Users table:
Stores user profile, email, password hash, and created date.

Categories table:
Stores user-specific income and expense categories.

Transactions table:
Stores user transactions with category relationship, amount, transaction type, date, description, and recurring status.

Relationships:

- One user has many categories.
- One user has many transactions.
- One category has many transactions.

## 10. Local Run Workflow

1. Start backend with `mvn spring-boot:run`.
2. Backend runs at `http://localhost:8080`.
3. Start frontend with `npm run dev`.
4. Frontend runs at `http://localhost:5173`.
5. Login using demo credentials or create a new account.
6. Use dashboard, transactions, categories, and reports.

## 11. Deployment Workflow

1. Build backend using `mvn clean package`.
2. Build frontend using `npm run build`.
3. Host backend JAR on a server with Java installed.
4. Host frontend `dist` folder on a static server.
5. Configure frontend API URL to point to the deployed backend.
6. Replace H2 with MySQL, PostgreSQL, or SQL Server for production use if required.

