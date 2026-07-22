package com.expensetracker.controller;

import com.expensetracker.dto.AuthDtos.*;
import com.expensetracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService auth;

  public AuthController(AuthService auth) {
    this.auth = auth;
  }

  @PostMapping("/register")
  public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    return auth.register(request);
  }

  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody LoginRequest request) {
    return auth.login(request);
  }

  @PostMapping("/logout")
  public MessageResponse logout(@RequestHeader("X-Auth-Token") String token) {
    auth.logout(token);
    return new MessageResponse("Logged out successfully.");
  }

  @PostMapping("/forgot-password")
  public MessageResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    return auth.forgotPassword(request);
  }
}
