package com.company.attendance.controller;

import com.company.attendance.dto.CreateUserRequest;
import com.company.attendance.dto.LeaveActionRequest;
import com.company.attendance.dto.LeaveResponse;
import com.company.attendance.dto.UserResponse;
import com.company.attendance.service.LeaveService;
import com.company.attendance.service.UserService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final LeaveService leaveService;

    public AdminController(UserService userService, LeaveService leaveService) {
        this.userService = userService;
        this.leaveService = leaveService;
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createEmployee(@Valid @RequestBody CreateUserRequest request) {
        return userService.createEmployee(request);
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/leaves/pending")
    public List<LeaveResponse> getPendingLeaves() {
        return leaveService.getPendingLeaves();
    }

    @PostMapping("/leaves/{leaveId}/approve")
    public ResponseEntity<LeaveResponse> approveLeave(
            @PathVariable Long leaveId,
            @Valid @RequestBody LeaveActionRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(leaveService.approveLeave(leaveId, principal.getName(), request));
    }

    @PostMapping("/leaves/{leaveId}/reject")
    public ResponseEntity<LeaveResponse> rejectLeave(
            @PathVariable Long leaveId,
            @Valid @RequestBody LeaveActionRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(leaveService.rejectLeave(leaveId, principal.getName(), request));
    }
}
