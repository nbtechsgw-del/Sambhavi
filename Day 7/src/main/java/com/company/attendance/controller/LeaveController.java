package com.company.attendance.controller;

import com.company.attendance.dto.LeaveRequestDto;
import com.company.attendance.dto.LeaveResponse;
import com.company.attendance.service.LeaveService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeaveResponse applyLeave(@Valid @RequestBody LeaveRequestDto request, Principal principal) {
        return leaveService.applyLeave(principal.getName(), request);
    }

    @GetMapping("/me")
    public List<LeaveResponse> getMyLeaves(Principal principal) {
        return leaveService.getMyLeaves(principal.getName());
    }
}
