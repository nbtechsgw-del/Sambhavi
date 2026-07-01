package com.tourism.app.repository;

import com.tourism.app.model.Hotel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByCityContainingIgnoreCaseOrNameContainingIgnoreCase(String city, String name);
    List<Hotel> findByCityIgnoreCase(String city);
    boolean existsByNameAndCity(String name, String city);
    Optional<Hotel> findByNameAndCity(String name, String city);
}
