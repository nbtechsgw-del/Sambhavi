package com.company.attendance.dto;

public record AuthResponse(
        String token,
        String username,
        String role
) {
}
