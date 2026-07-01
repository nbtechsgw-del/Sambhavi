package com.tourism.app.repository;

import com.tourism.app.model.Hotel;
import com.tourism.app.model.HotelReview;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelReviewRepository extends JpaRepository<HotelReview, Long> {
    List<HotelReview> findByHotelOrderByCreatedAtDesc(Hotel hotel);
    boolean existsByHotelAndAuthorName(Hotel hotel, String authorName);
    void deleteByHotel(Hotel hotel);
}
