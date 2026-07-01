package com.tourism.app.repository;

import com.tourism.app.model.Destination;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByNameContainingIgnoreCaseOrCountryContainingIgnoreCaseOrTypeContainingIgnoreCase(String name, String country, String type);
    boolean existsByNameAndCountry(String name, String country);
}
