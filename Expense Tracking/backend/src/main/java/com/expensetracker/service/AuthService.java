package com.expensetracker.service;

import com.expensetracker.dto.AuthDtos.*;
import com.expensetracker.model.AppUser;
import com.expensetracker.repository.UserRepository;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository users;
  private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  private final Map<String, Long> sessions = new ConcurrentHashMap<>();

  public AuthService(UserRepository users) {
    this.users = users;
  }

  public AuthResponse register(RegisterRequest request) {
    if (users.existsByEmailIgnoreCase(request.email())) {
      throw new IllegalArgumentException("Email is already registered.");
    }
    AppUser user = new AppUser();
    user.setFullName(request.fullName().trim());
    user.setEmail(request.email().trim().toLowerCase());
    user.setPasswordHash(encoder.encode(request.password()));
    users.save(user);
    return issueToken(user);
  }

  public AuthResponse login(LoginRequest request) {
    AppUser user = users.findByEmailIgnoreCase(request.email())
        .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
    if (!encoder.matches(request.password(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid email or password.");
    }
    return issueToken(user);
  }

  public void logout(String token) {
    sessions.remove(token);
  }

  public AppUser requireUser(String token) {
    Long userId = sessions.get(token);
    if (userId == null) {
      throw new SecurityException("Missing or invalid authentication token.");
    }
    return users.findById(userId).orElseThrow(() -> new SecurityException("User no longer exists."));
  }

  public MessageResponse forgotPassword(ForgotPasswordRequest request) {
    return new MessageResponse("If " + request.email() + " exists, a reset email would be sent. Email delivery is not configured for local demo mode.");
  }

  private AuthResponse issueToken(AppUser user) {
    String token = UUID.randomUUID().toString();
    sessions.put(token, user.getId());
    return new AuthResponse(token, toUser(user));
  }

  public UserResponse toUser(AppUser user) {
    return new UserResponse(user.getId(), user.getFullName(), user.getEmail());
  }
}
