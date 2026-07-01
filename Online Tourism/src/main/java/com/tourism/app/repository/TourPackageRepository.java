package com.tourism.app.repository;

import com.tourism.app.model.TourPackage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {
    List<TourPackage> findByTitleContainingIgnoreCaseOrDestinationContainingIgnoreCase(String title, String destination);
    boolean existsByTitle(String title);
}
