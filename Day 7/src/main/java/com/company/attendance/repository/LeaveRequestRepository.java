package com.company.attendance.repository;

import com.company.attendance.entity.LeaveStatus;
import com.company.attendance.entity.LeaveRequest;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findAllByUserIdOrderByAppliedAtDesc(Long userId);

    List<LeaveRequest> findAllByStatusOrderByAppliedAtAsc(LeaveStatus status);

    boolean existsByUserIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long userId,
            List<LeaveStatus> statuses,
            LocalDate endDate,
            LocalDate startDate
    );
}
