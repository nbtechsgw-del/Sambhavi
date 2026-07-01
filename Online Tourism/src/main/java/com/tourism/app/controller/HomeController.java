package com.tourism.app.controller;

import com.tourism.app.repository.DestinationRepository;
import com.tourism.app.repository.HotelRepository;
import com.tourism.app.repository.HotelReviewRepository;
import com.tourism.app.repository.HotelRoomTypeRepository;
import com.tourism.app.repository.TourPackageRepository;
import java.math.BigDecimal;
import java.util.stream.Stream;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final DestinationRepository destinationRepository;
    private final TourPackageRepository tourPackageRepository;
    private final HotelRepository hotelRepository;
    private final HotelReviewRepository hotelReviewRepository;
    private final HotelRoomTypeRepository hotelRoomTypeRepository;

    public HomeController(DestinationRepository destinationRepository, TourPackageRepository tourPackageRepository, HotelRepository hotelRepository,
                          HotelReviewRepository hotelReviewRepository, HotelRoomTypeRepository hotelRoomTypeRepository) {
        this.destinationRepository = destinationRepository;
        this.tourPackageRepository = tourPackageRepository;
        this.hotelRepository = hotelRepository;
        this.hotelReviewRepository = hotelReviewRepository;
        this.hotelRoomTypeRepository = hotelRoomTypeRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("packages", tourPackageRepository.findAll());
        model.addAttribute("hotels", hotelRepository.findAll());
        return "index";
    }

    @GetMapping("/packages")
    public String packages(@RequestParam(required = false) String keyword, Model model) {
        if (keyword == null || keyword.isBlank()) {
            model.addAttribute("packages", tourPackageRepository.findAll());
        } else {
            model.addAttribute("packages", tourPackageRepository.findByTitleContainingIgnoreCaseOrDestinationContainingIgnoreCase(keyword, keyword));
        }
        model.addAttribute("keyword", keyword);
        return "packages";
    }

    @GetMapping("/destinations")
    public String destinations(@RequestParam(required = false) String keyword, Model model) {
        if (keyword == null || keyword.isBlank()) {
            model.addAttribute("destinations", destinationRepository.findAll());
        } else {
            model.addAttribute("destinations", destinationRepository.findByNameContainingIgnoreCaseOrCountryContainingIgnoreCaseOrTypeContainingIgnoreCase(keyword, keyword, keyword));
        }
        model.addAttribute("keyword", keyword);
        return "destinations";
    }

    @GetMapping("/packages/{id}")
    public String packageDetails(@PathVariable Long id, Model model) {
        var tourPackage = tourPackageRepository.findById(id).orElseThrow();
        model.addAttribute("pkg", tourPackage);
        model.addAttribute("hotels", hotelRepository.findByCityIgnoreCase(tourPackage.getDestination()));
        return "package-details";
    }

    @GetMapping("/hotels")
    public String hotels(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) BigDecimal minPrice,
                         @RequestParam(required = false) BigDecimal maxPrice,
                         @RequestParam(required = false) String roomType,
                         Model model) {
        var hotels = hotelRepository.findAll();
        if (keyword == null || keyword.isBlank()) {
            hotels = hotelRepository.findAll();
        } else {
            hotels = hotelRepository.findByCityContainingIgnoreCaseOrNameContainingIgnoreCase(keyword, keyword);
        }
        Stream<com.tourism.app.model.Hotel> hotelStream = hotels.stream();
        if (minPrice != null) {
            hotelStream = hotelStream.filter(hotel -> hotel.getStartingPrice().compareTo(minPrice) >= 0);
        }
        if (maxPrice != null) {
            hotelStream = hotelStream.filter(hotel -> hotel.getStartingPrice().compareTo(maxPrice) <= 0);
        }
        if (roomType != null && !roomType.isBlank()) {
            hotelStream = hotelStream.filter(hotel -> hotel.getRoomTypes().stream()
                    .anyMatch(type -> roomType.equalsIgnoreCase(type.getTypeName())));
        }
        model.addAttribute("hotels", hotelStream.toList());
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("roomType", roomType);
        model.addAttribute("roomTypes", hotelRoomTypeRepository.findAll().stream()
                .map(com.tourism.app.model.HotelRoomType::getTypeName)
                .filter(type -> type != null && !type.isBlank())
                .distinct()
                .toList());
        return "hotels";
    }

    @GetMapping("/hotels/{id}")
    public String hotelDetails(@PathVariable Long id, Model model) {
        var hotel = hotelRepository.findById(id).orElseThrow();
        model.addAttribute("hotel", hotel);
        model.addAttribute("roomTypes", hotelRoomTypeRepository.findByHotelOrderByPricePerNightAsc(hotel));
        model.addAttribute("reviews", hotelReviewRepository.findByHotelOrderByCreatedAtDesc(hotel));
        return "hotel-details";
    }
}
