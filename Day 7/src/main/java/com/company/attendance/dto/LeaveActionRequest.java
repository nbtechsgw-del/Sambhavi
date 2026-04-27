package com.company.attendance.dto;

import jakarta.validation.constraints.Size;

public record LeaveActionRequest(
        @Size(max = 500) String comment
) {
}
