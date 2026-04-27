package com.company.attendance.service;

import com.company.attendance.dto.AttendanceResponse;
import com.company.attendance.entity.AttendanceRecord;
import com.company.attendance.entity.User;
import com.company.attendance.exception.BadRequestException;
import com.company.attendance.exception.ResourceNotFoundException;
import com.company.attendance.repository.AttendanceRecordRepository;
import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class AttendanceService {

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final UserService userService;
    private final Clock appClock;

    public AttendanceService(
            AttendanceRecordRepository attendanceRecordRepository,
            UserService userService,
            Clock appClock
    ) {
        this.attendanceRecordRepository = attendanceRecordRepository;
        this.userService = userService;
        this.appClock = appClock;
    }

    @Transactional
    public AttendanceResponse checkIn(String username) {
        User user = userService.getByUsername(username);
        LocalDate today = LocalDate.now(appClock);

        if (attendanceRecordRepository.existsByUserIdAndAttendanceDate(user.getId(), today)) {
            throw new BadRequestException("Attendance already marked for today");
        }

        AttendanceRecord record = AttendanceRecord.builder()
                .user(user)
                .attendanceDate(today)
                .checkInTime(LocalDateTime.now(appClock))
                .build();

        try {
            return toResponse(attendanceRecordRepository.save(record));
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("Attendance already marked for today");
        }
    }

    @Transactional
    public AttendanceResponse checkOut(String username) {
        User user = userService.getByUsername(username);
        LocalDate today = LocalDate.now(appClock);

        AttendanceRecord record = attendanceRecordRepository.findByUserIdAndAttendanceDate(user.getId(), today)
                .orElseThrow(() -> new ResourceNotFoundException("No check-in found for today"));

        if (record.getCheckOutTime() != null) {
            throw new BadRequestException("Attendance already checked out for today");
        }

        LocalDateTime checkOutTime = LocalDateTime.now(appClock);
        if (checkOutTime.isBefore(record.getCheckInTime())) {
            throw new BadRequestException("Check-out time cannot be before check-in time");
        }

        record.setCheckOutTime(checkOutTime);
        record.setWorkingMinutes(Duration.between(record.getCheckInTime(), checkOutTime).toMinutes());
        return toResponse(attendanceRecordRepository.save(record));
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getMyAttendance(String username) {
        User user = userService.getByUsername(username);
        return attendanceRecordRepository.findAllByUserIdOrderByAttendanceDateDesc(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    AttendanceResponse toResponse(AttendanceRecord record) {
        return new AttendanceResponse(
                record.getId(),
                record.getAttendanceDate(),
                record.getCheckInTime(),
                record.getCheckOutTime(),
                record.getWorkingMinutes(),
                formatWorkingHours(record.getWorkingMinutes())
        );
    }

    private String formatWorkingHours(Long workingMinutes) {
        if (workingMinutes == null) {
            return "PENDING_CHECK_OUT";
        }
        long hours = workingMinutes / 60;
        long minutes = workingMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}
