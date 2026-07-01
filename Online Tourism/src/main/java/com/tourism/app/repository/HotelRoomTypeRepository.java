package com.tourism.app.repository;

import com.tourism.app.model.Hotel;
import com.tourism.app.model.HotelRoomType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRoomTypeRepository extends JpaRepository<HotelRoomType, Long> {
    List<HotelRoomType> findByHotelOrderByPricePerNightAsc(Hotel hotel);
    void deleteByHotel(Hotel hotel);
}
