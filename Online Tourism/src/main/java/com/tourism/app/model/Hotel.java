package com.tourism.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String city;
    private Integer roomsAvailable;
    private BigDecimal pricePerNight;
    private String roomType;
    private Double rating;
    private String imageUrl;
    @Column(length = 1000)
    private String description;
    @OneToMany(mappedBy = "hotel")
    private List<HotelRoomType> roomTypes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getRoomsAvailable() {
        return roomsAvailable;
    }

    public void setRoomsAvailable(Integer roomsAvailable) {
        this.roomsAvailable = roomsAvailable;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        if (imageUrl == null || imageUrl.isBlank()) {
            return "https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80";
        }
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<HotelRoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(List<HotelRoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public BigDecimal getStartingPrice() {
        BigDecimal defaultPrice = pricePerNight == null ? BigDecimal.ZERO : pricePerNight;
        return roomTypes == null || roomTypes.isEmpty()
                ? defaultPrice
                : roomTypes.stream()
                        .map(HotelRoomType::getPricePerNight)
                        .filter(price -> price != null)
                        .min(Comparator.naturalOrder())
                        .orElse(defaultPrice);
    }

    public Integer getTotalRoomsAvailable() {
        Integer defaultRooms = roomsAvailable == null ? 0 : roomsAvailable;
        return roomTypes == null || roomTypes.isEmpty()
                ? defaultRooms
                : roomTypes.stream()
                        .map(HotelRoomType::getRoomsAvailable)
                        .filter(rooms -> rooms != null)
                        .reduce(0, Integer::sum);
    }

    public String getRoomTypeSummary() {
        if (roomTypes == null || roomTypes.isEmpty()) {
            return roomType == null || roomType.isBlank() ? "Standard" : roomType;
        }
        return roomTypes.stream()
                .map(HotelRoomType::getTypeName)
                .filter(type -> type != null && !type.isBlank())
                .distinct()
                .reduce((first, second) -> first + ", " + second)
                .orElse("Standard");
    }
}
