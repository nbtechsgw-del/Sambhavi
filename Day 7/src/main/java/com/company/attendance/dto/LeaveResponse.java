package com.company.attendance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveResponse(
        Long id,
        Long userId,
        String username,
        LocalDate startDate,
        LocalDate endDate,
        String reason,
        String status,
        LocalDateTime appliedAt,
        LocalDateTime reviewedAt,
        String reviewedBy,
        String reviewComment
) {
}
