package com.company.attendance.repository;

import com.company.attendance.entity.AttendanceRecord;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {

    boolean existsByUserIdAndAttendanceDate(Long userId, LocalDate attendanceDate);

    Optional<AttendanceRecord> findByUserIdAndAttendanceDate(Long userId, LocalDate attendanceDate);

    List<AttendanceRecord> findAllByUserIdOrderByAttendanceDateDesc(Long userId);
}
