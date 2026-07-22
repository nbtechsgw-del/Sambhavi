package com.expensetracker.service;

import com.expensetracker.dto.CategoryDtos.*;
import com.expensetracker.dto.ReportDtos.*;
import com.expensetracker.dto.TransactionDtos.*;
import com.expensetracker.model.*;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FinanceService {
  private final CategoryRepository categories;
  private final TransactionRepository transactions;

  public FinanceService(CategoryRepository categories, TransactionRepository transactions) {
    this.categories = categories;
    this.transactions = transactions;
  }

  public List<CategoryResponse> listCategories(AppUser user) {
    return categories.findByUserOrderByTypeAscNameAsc(user).stream().map(this::toCategory).toList();
  }

  public CategoryResponse createCategory(AppUser user, CategoryRequest request) {
    Category category = new Category();
    category.setUser(user);
    category.setName(request.name().trim());
    category.setType(request.type());
    return toCategory(categories.save(category));
  }

  public CategoryResponse updateCategory(AppUser user, Long id, CategoryRequest request) {
    Category category = findCategory(user, id);
    category.setName(request.name().trim());
    category.setType(request.type());
    return toCategory(categories.save(category));
  }

  @Transactional
  public void deleteCategory(AppUser user, Long id) {
    Category category = findCategory(user, id);
    boolean inUse = transactions.findByUserOrderByTransactionDateDescCreatedDateDesc(user).stream()
        .anyMatch(t -> t.getCategory().getId().equals(id));
    if (inUse) {
      throw new IllegalArgumentException("Category is used by transactions and cannot be deleted.");
    }
    categories.delete(category);
  }

  public List<TransactionResponse> listTransactions(AppUser user, LocalDate from, LocalDate to, Long categoryId, String type, String search) {
    String normalizedSearch = search == null || search.isBlank() ? null : search.trim();
    TransactionType normalizedType = type == null || type.isBlank() ? null : TransactionType.valueOf(type.toUpperCase());
    return transactions.search(user, from, to, categoryId, normalizedType, normalizedSearch).stream().map(this::toTransaction).toList();
  }

  public TransactionResponse createTransaction(AppUser user, TransactionRequest request) {
    FinancialTransaction transaction = new FinancialTransaction();
    applyTransaction(user, transaction, request);
    return toTransaction(transactions.save(transaction));
  }

  public TransactionResponse updateTransaction(AppUser user, Long id, TransactionRequest request) {
    FinancialTransaction transaction = transactions.findById(id)
        .filter(t -> t.getUser().getId().equals(user.getId()))
        .orElseThrow(() -> new IllegalArgumentException("Transaction not found."));
    applyTransaction(user, transaction, request);
    return toTransaction(transactions.save(transaction));
  }

  public void deleteTransaction(AppUser user, Long id) {
    FinancialTransaction transaction = transactions.findById(id)
        .filter(t -> t.getUser().getId().equals(user.getId()))
        .orElseThrow(() -> new IllegalArgumentException("Transaction not found."));
    transactions.delete(transaction);
  }

  public DashboardResponse dashboard(AppUser user) {
    List<FinancialTransaction> all = transactions.findByUserOrderByTransactionDateDescCreatedDateDesc(user);
    YearMonth now = YearMonth.now();
    BigDecimal totalIncome = sum(all, TransactionType.INCOME);
    BigDecimal totalExpenses = sum(all, TransactionType.EXPENSE);
    BigDecimal monthIncome = sum(all.stream().filter(t -> YearMonth.from(t.getTransactionDate()).equals(now)).toList(), TransactionType.INCOME);
    BigDecimal monthExpenses = sum(all.stream().filter(t -> YearMonth.from(t.getTransactionDate()).equals(now)).toList(), TransactionType.EXPENSE);
    List<TransactionResponse> recent = transactions.findTop8ByUserOrderByTransactionDateDescCreatedDateDesc(user).stream().map(this::toTransaction).toList();
    return new DashboardResponse(totalIncome, totalExpenses, totalIncome.subtract(totalExpenses), monthIncome, monthExpenses, recent);
  }

  public ReportResponse report(AppUser user, LocalDate from, LocalDate to) {
    List<FinancialTransaction> filtered = listEntities(user, from, to);
    Map<String, BigDecimal> monthly = new LinkedHashMap<>();
    Map<String, BigDecimal> byCategory = new LinkedHashMap<>();
    Map<String, BigDecimal> incomeVsExpense = new LinkedHashMap<>();
    incomeVsExpense.put("Income", BigDecimal.ZERO);
    incomeVsExpense.put("Expense", BigDecimal.ZERO);

    for (FinancialTransaction t : filtered) {
      if (t.getType() == TransactionType.EXPENSE) {
        monthly.merge(YearMonth.from(t.getTransactionDate()).toString(), t.getAmount(), BigDecimal::add);
        byCategory.merge(t.getCategory().getName(), t.getAmount(), BigDecimal::add);
        incomeVsExpense.merge("Expense", t.getAmount(), BigDecimal::add);
      } else {
        incomeVsExpense.merge("Income", t.getAmount(), BigDecimal::add);
      }
    }
    BigDecimal income = sum(filtered, TransactionType.INCOME);
    BigDecimal expense = sum(filtered, TransactionType.EXPENSE);
    return new ReportResponse(income, expense, income.subtract(expense), monthly, byCategory, incomeVsExpense);
  }

  private List<FinancialTransaction> listEntities(AppUser user, LocalDate from, LocalDate to) {
    return transactions.search(user, from, to, null, null, null);
  }

  private void applyTransaction(AppUser user, FinancialTransaction transaction, TransactionRequest request) {
    Category category = findCategory(user, request.categoryId());
    if (!category.getType().name().equals(request.type().name())) {
      throw new IllegalArgumentException("Category type must match transaction type.");
    }
    transaction.setUser(user);
    transaction.setCategory(category);
    transaction.setAmount(request.amount());
    transaction.setType(request.type());
    transaction.setDescription(request.description() == null ? "" : request.description().trim());
    transaction.setTransactionDate(request.transactionDate());
    transaction.setRecurring(request.recurring());
  }

  private Category findCategory(AppUser user, Long id) {
    return categories.findByIdAndUser(id, user).orElseThrow(() -> new IllegalArgumentException("Category not found."));
  }

  private BigDecimal sum(List<FinancialTransaction> items, TransactionType type) {
    return items.stream().filter(t -> t.getType() == type).map(FinancialTransaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private CategoryResponse toCategory(Category category) {
    return new CategoryResponse(category.getId(), category.getName(), category.getType());
  }

  private TransactionResponse toTransaction(FinancialTransaction t) {
    return new TransactionResponse(t.getId(), t.getCategory().getId(), t.getCategory().getName(), t.getAmount(), t.getType(), t.getDescription(), t.getTransactionDate(), t.isRecurring());
  }
}
