package com.company.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.company.attendance.dto.LeaveActionRequest;
import com.company.attendance.dto.LeaveRequestDto;
import com.company.attendance.dto.LeaveResponse;
import com.company.attendance.entity.LeaveRequest;
import com.company.attendance.entity.LeaveStatus;
import com.company.attendance.entity.Role;
import com.company.attendance.entity.User;
import com.company.attendance.exception.BadRequestException;
import com.company.attendance.repository.LeaveRequestRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LeaveServiceTest {

    @Mock
    private LeaveRequestRepository leaveRequestRepository;

    @Mock
    private UserService userService;

    private LeaveService leaveService;
    private User employee;
    private User admin;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-04-17T03:30:00Z"), ZoneId.of("Asia/Kolkata"));
        leaveService = new LeaveService(leaveRequestRepository, userService, clock);

        employee = User.builder()
                .id(5L)
                .username("employee")
                .email("employee@company.com")
                .password("secret")
                .role(Role.ROLE_EMPLOYEE)
                .active(true)
                .build();

        admin = User.builder()
                .id(1L)
                .username("admin")
                .email("admin@company.com")
                .password("secret")
                .role(Role.ROLE_ADMIN)
                .active(true)
                .build();
    }

    @Test
    void applyLeaveShouldRejectInvalidRange() {
        LeaveRequestDto request = new LeaveRequestDto(
                LocalDate.of(2026, 4, 20),
                LocalDate.of(2026, 4, 18),
                "Family event"
        );

        when(userService.getByUsername("employee")).thenReturn(employee);

        assertThatThrownBy(() -> leaveService.applyLeave("employee", request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("End date cannot be before start date");
    }

    @Test
    void approveLeaveShouldUpdateStatusAndReviewer() {
        LeaveRequest leaveRequest = LeaveRequest.builder()
                .id(11L)
                .user(employee)
                .startDate(LocalDate.of(2026, 4, 20))
                .endDate(LocalDate.of(2026, 4, 21))
                .reason("Medical leave")
                .status(LeaveStatus.PENDING)
                .build();

        when(leaveRequestRepository.findById(11L)).thenReturn(Optional.of(leaveRequest));
        when(userService.getByUsername("admin")).thenReturn(admin);
        when(leaveRequestRepository.save(any(LeaveRequest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LeaveResponse response = leaveService.approveLeave(11L, "admin", new LeaveActionRequest("Approved"));

        assertThat(response.status()).isEqualTo("APPROVED");
        assertThat(response.reviewedBy()).isEqualTo("admin");
        assertThat(response.reviewComment()).isEqualTo("Approved");
    }
}
