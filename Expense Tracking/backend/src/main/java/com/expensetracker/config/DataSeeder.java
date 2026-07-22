package com.expensetracker.config;

import com.expensetracker.dto.CategoryDtos.CategoryRequest;
import com.expensetracker.dto.TransactionDtos.TransactionRequest;
import com.expensetracker.model.CategoryType;
import com.expensetracker.model.TransactionType;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.AuthService;
import com.expensetracker.service.FinanceService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
  private final UserRepository users;
  private final AuthService auth;
  private final FinanceService finance;

  public DataSeeder(UserRepository users, AuthService auth, FinanceService finance) {
    this.users = users;
    this.auth = auth;
    this.finance = finance;
  }

  @Override
  public void run(String... args) {
    if (users.existsByEmailIgnoreCase("demo@expense.local")) {
      return;
    }
    var session = auth.register(new com.expensetracker.dto.AuthDtos.RegisterRequest("Demo User", "demo@expense.local", "password"));
    var user = auth.requireUser(session.token());
    var salary = finance.createCategory(user, new CategoryRequest("Salary", CategoryType.INCOME));
    var freelance = finance.createCategory(user, new CategoryRequest("Freelance", CategoryType.INCOME));
    var food = finance.createCategory(user, new CategoryRequest("Food", CategoryType.EXPENSE));
    var rent = finance.createCategory(user, new CategoryRequest("Rent", CategoryType.EXPENSE));
    var travel = finance.createCategory(user, new CategoryRequest("Travel", CategoryType.EXPENSE));
    finance.createTransaction(user, new TransactionRequest(salary.id(), new BigDecimal("62000"), TransactionType.INCOME, "Monthly salary", LocalDate.now().withDayOfMonth(1), true));
    finance.createTransaction(user, new TransactionRequest(freelance.id(), new BigDecimal("8500"), TransactionType.INCOME, "Landing page project", LocalDate.now().minusDays(8), false));
    finance.createTransaction(user, new TransactionRequest(rent.id(), new BigDecimal("18000"), TransactionType.EXPENSE, "Apartment rent", LocalDate.now().withDayOfMonth(3), true));
    finance.createTransaction(user, new TransactionRequest(food.id(), new BigDecimal("2450"), TransactionType.EXPENSE, "Groceries", LocalDate.now().minusDays(2), false));
    finance.createTransaction(user, new TransactionRequest(travel.id(), new BigDecimal("1200"), TransactionType.EXPENSE, "Metro card recharge", LocalDate.now().minusDays(5), false));
    auth.logout(session.token());
  }
}
