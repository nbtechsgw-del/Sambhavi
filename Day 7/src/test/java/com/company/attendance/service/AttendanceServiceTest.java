package com.company.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.company.attendance.dto.AttendanceResponse;
import com.company.attendance.entity.AttendanceRecord;
import com.company.attendance.entity.Role;
import com.company.attendance.entity.User;
import com.company.attendance.exception.BadRequestException;
import com.company.attendance.exception.ResourceNotFoundException;
import com.company.attendance.repository.AttendanceRecordRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock
    private AttendanceRecordRepository attendanceRecordRepository;

    @Mock
    private UserService userService;

    private AttendanceService attendanceService;
    private User employee;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-04-17T03:30:00Z"), ZoneId.of("Asia/Kolkata"));
        attendanceService = new AttendanceService(attendanceRecordRepository, userService, clock);

        employee = User.builder()
                .id(10L)
                .username("employee")
                .email("employee@company.com")
                .password("secret")
                .role(Role.ROLE_EMPLOYEE)
                .active(true)
                .build();
    }

    @Test
    void checkInShouldPreventDuplicateAttendanceForSameDay() {
        when(userService.getByUsername("employee")).thenReturn(employee);
        when(attendanceRecordRepository.existsByUserIdAndAttendanceDate(10L, LocalDate.of(2026, 4, 17)))
                .thenReturn(true);

        assertThatThrownBy(() -> attendanceService.checkIn("employee"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Attendance already marked for today");

        verify(attendanceRecordRepository, never()).save(any());
    }

    @Test
    void checkOutShouldCalculateWorkingHours() {
        AttendanceRecord record = AttendanceRecord.builder()
                .id(1L)
                .user(employee)
                .attendanceDate(LocalDate.of(2026, 4, 17))
                .checkInTime(LocalDateTime.of(2026, 4, 17, 8, 0))
                .build();

        when(userService.getByUsername("employee")).thenReturn(employee);
        when(attendanceRecordRepository.findByUserIdAndAttendanceDate(10L, LocalDate.of(2026, 4, 17)))
                .thenReturn(Optional.of(record));
        when(attendanceRecordRepository.save(any(AttendanceRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AttendanceResponse response = attendanceService.checkOut("employee");

        assertThat(response.workingMinutes()).isEqualTo(60L);
        assertThat(response.workingHours()).isEqualTo("01:00");
    }

    @Test
    void checkOutShouldFailWhenNoCheckInExists() {
        when(userService.getByUsername("employee")).thenReturn(employee);
        when(attendanceRecordRepository.findByUserIdAndAttendanceDate(10L, LocalDate.of(2026, 4, 17)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> attendanceService.checkOut("employee"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No check-in found for today");
    }
}
