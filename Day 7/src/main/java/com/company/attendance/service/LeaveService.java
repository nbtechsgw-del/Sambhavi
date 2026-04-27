package com.company.attendance.service;

import com.company.attendance.dto.LeaveActionRequest;
import com.company.attendance.dto.LeaveRequestDto;
import com.company.attendance.dto.LeaveResponse;
import com.company.attendance.entity.LeaveRequest;
import com.company.attendance.entity.LeaveStatus;
import com.company.attendance.entity.User;
import com.company.attendance.exception.BadRequestException;
import com.company.attendance.exception.ResourceNotFoundException;
import com.company.attendance.repository.LeaveRequestRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserService userService;
    private final Clock appClock;

    public LeaveService(LeaveRequestRepository leaveRequestRepository, UserService userService, Clock appClock) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userService = userService;
        this.appClock = appClock;
    }

    @Transactional
    public LeaveResponse applyLeave(String username, LeaveRequestDto request) {
        User user = userService.getByUsername(username);
        validateDateRange(request);

        boolean overlapping = leaveRequestRepository
                .existsByUserIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        user.getId(),
                        List.of(LeaveStatus.PENDING, LeaveStatus.APPROVED),
                        request.endDate(),
                        request.startDate()
                );
        if (overlapping) {
            throw new BadRequestException("Overlapping leave request already exists");
        }

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .user(user)
                .startDate(request.startDate())
                .endDate(request.endDate())
                .reason(request.reason())
                .status(LeaveStatus.PENDING)
                .appliedAt(LocalDateTime.now(appClock))
                .build();

        return toResponse(leaveRequestRepository.save(leaveRequest));
    }

    @Transactional(readOnly = true)
    public List<LeaveResponse> getMyLeaves(String username) {
        User user = userService.getByUsername(username);
        return leaveRequestRepository.findAllByUserIdOrderByAppliedAtDesc(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LeaveResponse> getPendingLeaves() {
        return leaveRequestRepository.findAllByStatusOrderByAppliedAtAsc(LeaveStatus.PENDING).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public LeaveResponse approveLeave(Long leaveId, String adminUsername, LeaveActionRequest request) {
        return reviewLeave(leaveId, adminUsername, request, LeaveStatus.APPROVED);
    }

    @Transactional
    public LeaveResponse rejectLeave(Long leaveId, String adminUsername, LeaveActionRequest request) {
        return reviewLeave(leaveId, adminUsername, request, LeaveStatus.REJECTED);
    }

    private LeaveResponse reviewLeave(Long leaveId, String adminUsername, LeaveActionRequest request, LeaveStatus status) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new BadRequestException("Only pending leave requests can be reviewed");
        }

        User admin = userService.getByUsername(adminUsername);
        leaveRequest.setStatus(status);
        leaveRequest.setReviewedAt(LocalDateTime.now(appClock));
        leaveRequest.setReviewedBy(admin);
        leaveRequest.setReviewComment(request.comment());

        return toResponse(leaveRequestRepository.save(leaveRequest));
    }

    private void validateDateRange(LeaveRequestDto request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }
    }

    private LeaveResponse toResponse(LeaveRequest leaveRequest) {
        return new LeaveResponse(
                leaveRequest.getId(),
                leaveRequest.getUser().getId(),
                leaveRequest.getUser().getUsername(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getReason(),
                leaveRequest.getStatus().name(),
                leaveRequest.getAppliedAt(),
                leaveRequest.getReviewedAt(),
                leaveRequest.getReviewedBy() != null ? leaveRequest.getReviewedBy().getUsername() : null,
                leaveRequest.getReviewComment()
        );
    }
}
