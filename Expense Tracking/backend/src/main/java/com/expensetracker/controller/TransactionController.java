package com.expensetracker.controller;

import com.expensetracker.dto.TransactionDtos.*;
import com.expensetracker.model.AppUser;
import com.expensetracker.service.AuthService;
import com.expensetracker.service.FinanceService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
  private final AuthService auth;
  private final FinanceService finance;

  public TransactionController(AuthService auth, FinanceService finance) {
    this.auth = auth;
    this.finance = finance;
  }

  @GetMapping
  public List<TransactionResponse> list(
      @RequestHeader("X-Auth-Token") String token,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) String type,
      @RequestParam(required = false) String search) {
    return finance.listTransactions(user(token), from, to, categoryId, type, search);
  }

  @PostMapping
  public TransactionResponse create(@RequestHeader("X-Auth-Token") String token, @Valid @RequestBody TransactionRequest request) {
    return finance.createTransaction(user(token), request);
  }

  @PutMapping("/{id}")
  public TransactionResponse update(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
    return finance.updateTransaction(user(token), id, request);
  }

  @DeleteMapping("/{id}")
  public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable Long id) {
    finance.deleteTransaction(user(token), id);
  }

  private AppUser user(String token) {
    return auth.requireUser(token);
  }
}
