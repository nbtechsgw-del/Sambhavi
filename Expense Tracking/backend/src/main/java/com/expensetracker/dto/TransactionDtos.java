package com.expensetracker.dto;

import com.expensetracker.model.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDtos {
  public record TransactionRequest(
      @NotNull Long categoryId,
      @NotNull @DecimalMin("0.01") BigDecimal amount,
      @NotNull TransactionType type,
      String description,
      @NotNull LocalDate transactionDate,
      boolean recurring) {}

  public record TransactionResponse(
      Long id,
      Long categoryId,
      String categoryName,
      BigDecimal amount,
      TransactionType type,
      String description,
      LocalDate transactionDate,
      boolean recurring) {}
}
