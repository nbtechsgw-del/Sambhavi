package com.tourism.app.repository;

import com.tourism.app.model.Booking;
import com.tourism.app.model.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);
    void deleteByBooking(Booking booking);
}
