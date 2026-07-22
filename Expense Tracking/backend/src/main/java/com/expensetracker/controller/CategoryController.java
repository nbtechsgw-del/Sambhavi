package com.expensetracker.controller;

import com.expensetracker.dto.CategoryDtos.*;
import com.expensetracker.model.AppUser;
import com.expensetracker.service.AuthService;
import com.expensetracker.service.FinanceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
  private final AuthService auth;
  private final FinanceService finance;

  public CategoryController(AuthService auth, FinanceService finance) {
    this.auth = auth;
    this.finance = finance;
  }

  @GetMapping
  public List<CategoryResponse> list(@RequestHeader("X-Auth-Token") String token) {
    return finance.listCategories(user(token));
  }

  @PostMapping
  public CategoryResponse create(@RequestHeader("X-Auth-Token") String token, @Valid @RequestBody CategoryRequest request) {
    return finance.createCategory(user(token), request);
  }

  @PutMapping("/{id}")
  public CategoryResponse update(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
    return finance.updateCategory(user(token), id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id) {
    finance.deleteCategory(user(token), id);
  }

  private AppUser user(String token) {
    return auth.requireUser(token);
  }
}
