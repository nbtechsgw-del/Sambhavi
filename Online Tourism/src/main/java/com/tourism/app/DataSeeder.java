package com.tourism.app;

import com.tourism.app.model.Destination;
import com.tourism.app.model.Hotel;
import com.tourism.app.model.HotelReview;
import com.tourism.app.model.HotelRoomType;
import com.tourism.app.model.TourPackage;
import com.tourism.app.model.User;
import com.tourism.app.repository.DestinationRepository;
import com.tourism.app.repository.HotelRepository;
import com.tourism.app.repository.HotelReviewRepository;
import com.tourism.app.repository.HotelRoomTypeRepository;
import com.tourism.app.repository.TourPackageRepository;
import com.tourism.app.repository.UserRepository;
import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final TourPackageRepository tourPackageRepository;
    private final HotelRepository hotelRepository;
    private final HotelReviewRepository hotelReviewRepository;
    private final HotelRoomTypeRepository hotelRoomTypeRepository;

    public DataSeeder(UserRepository userRepository, DestinationRepository destinationRepository, TourPackageRepository tourPackageRepository, HotelRepository hotelRepository,
                      HotelReviewRepository hotelReviewRepository, HotelRoomTypeRepository hotelRoomTypeRepository) {
        this.userRepository = userRepository;
        this.destinationRepository = destinationRepository;
        this.tourPackageRepository = tourPackageRepository;
        this.hotelRepository = hotelRepository;
        this.hotelReviewRepository = hotelReviewRepository;
        this.hotelRoomTypeRepository = hotelRoomTypeRepository;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedDestinations();
        seedPackages();
        seedHotels();
        seedHotelReviews();
    }

    private void seedDestinations() {
        saveDestinationIfMissing(destination("Goa", "India", "Beach", "October to March", "Golden beaches, forts, nightlife and relaxed coastal stays."));
        saveDestinationIfMissing(destination("Kerala", "India", "Nature", "September to March", "Backwaters, hill stations, houseboats and calm green escapes."));
        saveDestinationIfMissing(destination("Dubai", "UAE", "International", "November to February", "Luxury shopping, desert safaris, skyscrapers and marina experiences."));
        saveDestinationIfMissing(destination("Jaipur", "India", "Heritage", "October to March", "Royal forts, palaces, markets and colourful Rajasthani culture."));
        saveDestinationIfMissing(destination("Paris", "France", "International", "April to June", "Museums, river cruises, architecture and romantic city walks."));
    }

    private void saveDestinationIfMissing(Destination destination) {
        if (!destinationRepository.existsByNameAndCountry(destination.getName(), destination.getCountry())) {
            destinationRepository.save(destination);
        }
    }

    private void seedUsers() {
        if (!userRepository.existsByEmail("admin@tourism.com")) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@tourism.com");
            admin.setPassword("admin123");
            admin.setPhone("9999999999");
            admin.setAddress("Head Office");
            admin.setPersonalCode("ADMIN2026");
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
        }
        if (!userRepository.existsByEmail("rahul@example.com")) {
            User user = new User();
            user.setName("Rahul Sharma");
            user.setEmail("rahul@example.com");
            user.setPassword("user123");
            user.setPhone("9876543210");
            user.setAddress("Mumbai, India");
            userRepository.save(user);
        }
    }

    private void seedPackages() {
        savePackageIfMissing(packageItem("Goa Beach Escape", "Goa", "Domestic", 4, "14999.00", 25,
                "10% summer discount", "Day 1 arrival, Day 2 North Goa, Day 3 beach activities, Day 4 departure.",
                "https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=900&q=80"));
        savePackageIfMissing(packageItem("Kerala Backwater Bliss", "Kerala", "Domestic", 5, "21999.00", 18,
                "Free houseboat dinner", "Cochin, Munnar, Alleppey houseboat and local sightseeing.",
                "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?auto=format&fit=crop&w=900&q=80"));
        savePackageIfMissing(packageItem("Dubai Luxury Tour", "Dubai", "International", 6, "74999.00", 12,
                "Desert safari included", "City tour, Burj Khalifa, desert safari, marina cruise and shopping.",
                "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?auto=format&fit=crop&w=900&q=80"));
        savePackageIfMissing(packageItem("Himalayan Adventure", "Manali", "Domestic", 5, "18999.00", 20,
                "Adventure combo included", "Solang Valley, Hadimba Temple, local market, rafting and camp night.",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=900&q=80"));
        savePackageIfMissing(packageItem("Jaipur Royal Heritage", "Jaipur", "Domestic", 3, "11999.00", 30,
                "Free heritage walk", "Amber Fort, City Palace, Hawa Mahal, local bazaar and Rajasthani dinner.",
                "https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=900&q=80"));
        savePackageIfMissing(packageItem("Kashmir Paradise Tour", "Srinagar", "Domestic", 6, "32999.00", 14,
                "Shikara ride included", "Srinagar, Gulmarg, Pahalgam, Dal Lake stay and mountain sightseeing.",
                "https://images.unsplash.com/photo-1598091383021-15ddea10925d?auto=format&fit=crop&w=900&q=80"));
        savePackageIfMissing(packageItem("Andaman Island Holiday", "Port Blair", "Domestic", 5, "38999.00", 16,
                "Free snorkeling session", "Cellular Jail, Havelock Island, Radhanagar Beach and water activities.",
                "https://images.unsplash.com/photo-1559827260-dc66d52bef19?auto=format&fit=crop&w=900&q=80"));
        savePackageIfMissing(packageItem("Singapore Family Fun", "Singapore", "International", 5, "68999.00", 18,
                "Universal Studios pass", "Sentosa, Gardens by the Bay, city tour, night safari and shopping.",
                "https://images.unsplash.com/photo-1525625293386-3f8f99389edd?auto=format&fit=crop&w=900&q=80"));
        savePackageIfMissing(packageItem("Thailand Island Hopper", "Phuket", "International", 6, "55999.00", 22,
                "Phi Phi island tour", "Phuket beaches, island cruise, local markets and cultural evening.",
                "https://images.unsplash.com/photo-1508009603885-50cf7c579365?auto=format&fit=crop&w=900&q=80"));
        savePackageIfMissing(packageItem("Paris Romance Escape", "Paris", "International", 7, "139999.00", 10,
                "Seine cruise included", "Eiffel Tower, Louvre, Versailles, Seine cruise and leisure shopping.",
                "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=900&q=80"));
    }

    private void seedHotels() {
        saveHotelIfMissing(hotel("Sea View Resort", "Goa", 16, "4500.00", "Deluxe", 4.5, "Beach-side resort with pool, restaurant and airport pickup."));
        saveHotelIfMissing(hotel("Palm Coast Inn", "Goa", 22, "3200.00", "Standard", 4.1, "Budget-friendly stay near popular beaches and cafes."));
        saveHotelIfMissing(hotel("Green Valley Stay", "Kerala", 10, "3800.00", "Deluxe", 4.3, "Hill-view rooms near tea gardens with breakfast."));
        saveHotelIfMissing(hotel("Backwater Pearl", "Kerala", 12, "5200.00", "Premium", 4.6, "Premium stay with houseboat booking and lake-view dining."));
        saveHotelIfMissing(hotel("Marina Grand Hotel", "Dubai", 8, "9200.00", "Suite", 4.8, "Premium hotel near Dubai Marina with city tour assistance."));
        saveHotelIfMissing(hotel("Desert Crown Suites", "Dubai", 15, "7800.00", "Suite", 4.4, "Modern suites with desert safari and airport transfer support."));
        saveHotelIfMissing(hotel("Snow Peak Inn", "Manali", 14, "3200.00", "Standard", 4.2, "Cozy mountain hotel with check-in and check-out support."));
        saveHotelIfMissing(hotel("Royal Haveli Palace", "Jaipur", 18, "4100.00", "Family", 4.5, "Heritage-style rooms close to Jaipur landmarks."));
        saveHotelIfMissing(hotel("Dal Lake Retreat", "Srinagar", 9, "6200.00", "Premium", 4.7, "Lake-facing hotel with shikara and local sightseeing support."));
        saveHotelIfMissing(hotel("Island Blue Resort", "Port Blair", 11, "5900.00", "Deluxe", 4.4, "Island resort with ferry coordination and beach transfers."));
        saveHotelIfMissing(hotel("Orchard City Hotel", "Singapore", 20, "11200.00", "Family", 4.6, "Central city hotel near metro, shopping and family attractions."));
        saveHotelIfMissing(hotel("Patong Bay Resort", "Phuket", 17, "6500.00", "Deluxe", 4.3, "Beach resort with island tour desk and breakfast."));
        saveHotelIfMissing(hotel("Eiffel View Stay", "Paris", 7, "14800.00", "Premium", 4.8, "Boutique hotel with city passes and Seine cruise help."));
    }

    private void savePackageIfMissing(TourPackage tourPackage) {
        if (!tourPackageRepository.existsByTitle(tourPackage.getTitle())) {
            tourPackageRepository.save(tourPackage);
        }
    }

    private void saveHotelIfMissing(Hotel hotel) {
        if (hotelRepository.existsByNameAndCity(hotel.getName(), hotel.getCity())) {
            hotelRepository.findByNameAndCity(hotel.getName(), hotel.getCity()).ifPresent(existingHotel -> {
                existingHotel.setImageUrl(hotel.getImageUrl());
                existingHotel.setRoomType(hotel.getRoomType());
                hotelRepository.save(existingHotel);
                seedRoomTypesIfMissing(existingHotel);
            });
        } else {
            seedRoomTypesIfMissing(hotelRepository.save(hotel));
        }
    }

    private void seedRoomTypesIfMissing(Hotel hotel) {
        if (!hotelRoomTypeRepository.findByHotelOrderByPricePerNightAsc(hotel).isEmpty()) {
            return;
        }
        String primaryType = hotel.getRoomType() == null || hotel.getRoomType().isBlank() ? "Standard" : hotel.getRoomType();
        saveRoomType(hotel, primaryType, hotel.getRoomsAvailable(), hotel.getPricePerNight());
        saveRoomType(hotel, "Suite".equalsIgnoreCase(primaryType) ? "Premium" : "Suite",
                Math.max(1, hotel.getRoomsAvailable() / 5), hotel.getPricePerNight().multiply(new BigDecimal("1.60")));
        saveRoomType(hotel, "Family".equalsIgnoreCase(primaryType) ? "Deluxe" : "Family",
                Math.max(2, hotel.getRoomsAvailable() / 3), hotel.getPricePerNight().multiply(new BigDecimal("1.25")));
    }

    private void saveRoomType(Hotel hotel, String typeName, Integer rooms, BigDecimal price) {
        HotelRoomType roomType = new HotelRoomType();
        roomType.setHotel(hotel);
        roomType.setTypeName(typeName == null || typeName.isBlank() ? "Standard" : typeName);
        roomType.setRoomsAvailable(rooms);
        roomType.setPricePerNight(price);
        hotelRoomTypeRepository.save(roomType);
    }

    private void seedHotelReviews() {
        hotelRepository.findAll().forEach(hotel -> {
            saveReviewIfMissing(hotel, "Aarav Mehta", 5, "Smooth check-in, clean rooms and the travel desk helped us plan local sightseeing.");
            saveReviewIfMissing(hotel, "Priya Nair", 4, "Comfortable stay with friendly staff. The location made our trip much easier.");
            saveReviewIfMissing(hotel, "Rohan Kapoor", 5, "Good value for the price and the room matched the photos shown online.");
        });
    }

    private void saveReviewIfMissing(Hotel hotel, String authorName, Integer rating, String comment) {
        if (!hotelReviewRepository.existsByHotelAndAuthorName(hotel, authorName)) {
            HotelReview review = new HotelReview();
            review.setHotel(hotel);
            review.setAuthorName(authorName);
            review.setRating(rating);
            review.setComment(comment);
            hotelReviewRepository.save(review);
        }
    }

    private TourPackage packageItem(String title, String destination, String category, Integer days, String price,
                                    Integer seats, String offer, String itinerary, String imageUrl) {
        TourPackage tourPackage = new TourPackage();
        tourPackage.setTitle(title);
        tourPackage.setDestination(destination);
        tourPackage.setCategory(category);
        tourPackage.setDurationDays(days);
        tourPackage.setPrice(new BigDecimal(price));
        tourPackage.setAvailableSeats(seats);
        tourPackage.setSeasonOffer(offer);
        tourPackage.setItinerary(itinerary);
        tourPackage.setImageUrl(imageUrl);
        return tourPackage;
    }

    private Hotel hotel(String name, String city, Integer rooms, String price, String roomType, Double rating, String description) {
        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setCity(city);
        hotel.setRoomsAvailable(rooms);
        hotel.setPricePerNight(new BigDecimal(price));
        hotel.setRoomType(roomType);
        hotel.setRating(rating);
        hotel.setDescription(description);
        hotel.setImageUrl(imageForCity(city));
        return hotel;
    }

    private Destination destination(String name, String country, String type, String season, String description) {
        Destination destination = new Destination();
        destination.setName(name);
        destination.setCountry(country);
        destination.setType(type);
        destination.setBestSeason(season);
        destination.setDescription(description);
        destination.setImageUrl(imageForCity(name));
        return destination;
    }

    private String imageForCity(String city) {
        return switch (city) {
            case "Goa" -> "https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=1200&q=80";
            case "Kerala" -> "https://images.unsplash.com/photo-1596176530529-78163a4f7af2?auto=format&fit=crop&w=1200&q=80";
            case "Dubai" -> "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80";
            case "Manali" -> "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=1200&q=80";
            case "Jaipur" -> "https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=1200&q=80";
            case "Srinagar" -> "https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1200&q=80";
            case "Port Blair" -> "https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=80";
            case "Singapore" -> "https://images.unsplash.com/photo-1445019980597-93fa8acb246c?auto=format&fit=crop&w=1200&q=80";
            case "Phuket" -> "https://images.unsplash.com/photo-1568084680786-a84f91d1153c?auto=format&fit=crop&w=1200&q=80";
            case "Paris" -> "https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=1200&q=80";
            default -> "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80";
        };
    }
}
