package com.tourism.app.repository;

import com.tourism.app.model.Booking;
import com.tourism.app.model.Hotel;
import com.tourism.app.model.TourPackage;
import com.tourism.app.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserOrderByIdDesc(User user);
    List<Booking> findByTourPackage(TourPackage tourPackage);
    List<Booking> findByHotel(Hotel hotel);
}
