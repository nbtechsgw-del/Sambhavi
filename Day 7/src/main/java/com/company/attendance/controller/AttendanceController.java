package com.company.attendance.controller;

import com.company.attendance.dto.AttendanceResponse;
import com.company.attendance.service.AttendanceService;
import java.security.Principal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<AttendanceResponse> checkIn(Principal principal) {
        return ResponseEntity.ok(attendanceService.checkIn(principal.getName()));
    }

    @PostMapping("/check-out")
    public ResponseEntity<AttendanceResponse> checkOut(Principal principal) {
        return ResponseEntity.ok(attendanceService.checkOut(principal.getName()));
    }

    @GetMapping("/me")
    public List<AttendanceResponse> getMyAttendance(Principal principal) {
        return attendanceService.getMyAttendance(principal.getName());
    }
}
