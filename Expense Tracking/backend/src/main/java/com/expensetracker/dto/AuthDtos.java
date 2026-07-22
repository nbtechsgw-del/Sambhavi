package com.expensetracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
  public record RegisterRequest(@NotBlank String fullName, @Email @NotBlank String email, @Size(min = 6) String password) {}
  public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
  public record AuthResponse(String token, UserResponse user) {}
  public record UserResponse(Long id, String fullName, String email) {}
  public record ForgotPasswordRequest(@Email @NotBlank String email) {}
  public record MessageResponse(String message) {}
}
