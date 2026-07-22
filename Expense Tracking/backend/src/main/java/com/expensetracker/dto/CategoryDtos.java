package com.expensetracker.dto;

import com.expensetracker.model.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryDtos {
  public record CategoryRequest(@NotBlank String name, @NotNull CategoryType type) {}
  public record CategoryResponse(Long id, String name, CategoryType type) {}
}
