package com.tourism.app.repository;

import com.tourism.app.model.Hotel;
import com.tourism.app.model.HotelBooking;
import com.tourism.app.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
    List<HotelBooking> findByUserOrderByIdDesc(User user);
    void deleteByHotel(Hotel hotel);
}
