package com.expensetracker.dto;

import com.expensetracker.dto.TransactionDtos.TransactionResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class ReportDtos {
  public record DashboardResponse(
      BigDecimal totalIncome,
      BigDecimal totalExpenses,
      BigDecimal currentBalance,
      BigDecimal monthIncome,
      BigDecimal monthExpenses,
      List<TransactionResponse> recentTransactions) {}

  public record ReportResponse(
      BigDecimal totalIncome,
      BigDecimal totalExpenses,
      BigDecimal balance,
      Map<String, BigDecimal> monthlyExpenses,
      Map<String, BigDecimal> categoryExpenses,
      Map<String, BigDecimal> incomeVsExpense) {}
}
