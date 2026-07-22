package com.expensetracker.controller;

import com.expensetracker.dto.ReportDtos.*;
import com.expensetracker.model.AppUser;
import com.expensetracker.service.AuthService;
import com.expensetracker.service.FinanceService;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReportController {
  private final AuthService auth;
  private final FinanceService finance;

  public ReportController(AuthService auth, FinanceService finance) {
    this.auth = auth;
    this.finance = finance;
  }

  @GetMapping("/dashboard")
  public DashboardResponse dashboard(@RequestHeader("X-Auth-Token") String token) {
    return finance.dashboard(user(token));
  }

  @GetMapping("/reports")
  public ReportResponse reports(
      @RequestHeader("X-Auth-Token") String token,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
    return finance.report(user(token), from, to);
  }

  private AppUser user(String token) {
    return auth.requireUser(token);
  }
}
