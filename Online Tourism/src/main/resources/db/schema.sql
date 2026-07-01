CREATE DATABASE IF NOT EXISTS tourism_db;
USE tourism_db;

DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS hotel_reviews;
DROP TABLE IF EXISTS hotel_bookings;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS hotel_room_types;
DROP TABLE IF EXISTS hotels;
DROP TABLE IF EXISTS tour_packages;
DROP TABLE IF EXISTS destinations;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(255),
    personal_code VARCHAR(100),
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER'
);

CREATE TABLE destinations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL,
    country VARCHAR(100) NOT NULL,
    type VARCHAR(80) NOT NULL,
    best_season VARCHAR(120),
    image_url VARCHAR(500),
    description TEXT
);

CREATE TABLE tour_packages (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    destination VARCHAR(120) NOT NULL,
    category VARCHAR(40) NOT NULL,
    duration_days INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available_seats INT NOT NULL,
    season_offer VARCHAR(120),
    image_url VARCHAR(500),
    itinerary TEXT
);

CREATE TABLE hotels (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    city VARCHAR(100) NOT NULL,
    rooms_available INT NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    room_type VARCHAR(80) NOT NULL DEFAULT 'Standard',
    rating DOUBLE,
    image_url VARCHAR(500),
    description TEXT
);

CREATE TABLE hotel_room_types (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    type_name VARCHAR(80) NOT NULL,
    rooms_available INT NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_room_type_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

CREATE TABLE hotel_bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    hotel_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    room_type_name VARCHAR(80),
    room_price_per_night DECIMAL(10, 2),
    rooms INT NOT NULL,
    guests INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('CONFIRMED', 'CANCELLED') NOT NULL DEFAULT 'CONFIRMED',
    CONSTRAINT fk_hotel_booking_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_hotel_booking_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

CREATE TABLE hotel_reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    hotel_id BIGINT NOT NULL,
    author_name VARCHAR(120) NOT NULL,
    rating INT NOT NULL,
    comment TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_hotel_review_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

CREATE TABLE bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    tour_package_id BIGINT NOT NULL,
    hotel_id BIGINT,
    travel_date DATE NOT NULL,
    travellers INT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status ENUM('PENDING', 'PAID', 'APPROVED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_booking_package FOREIGN KEY (tour_package_id) REFERENCES tour_packages(id),
    CONSTRAINT fk_booking_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id)
);

CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    transaction_id VARCHAR(100),
    gateway_provider VARCHAR(40),
    gateway_order_id VARCHAR(100),
    gateway_payment_id VARCHAR(100),
    gateway_signature VARCHAR(255),
    refund_id VARCHAR(100),
    paid_at DATETIME NOT NULL,
    status ENUM('PENDING', 'SUCCESS', 'REFUNDED', 'FAILED') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

CREATE TABLE feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    rating INT NOT NULL,
    subject VARCHAR(120) NOT NULL,
    message TEXT NOT NULL,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_feedback_user FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO users (name, email, password, phone, address, personal_code, role) VALUES
('System Admin', 'admin@tourism.com', 'admin123', '9999999999', 'Head Office', 'ADMIN2026', 'ADMIN'),
('Rahul Sharma', 'rahul@example.com', 'user123', '9876543210', 'Mumbai, India', NULL, 'USER');

INSERT INTO destinations (name, country, type, best_season, image_url, description) VALUES
('Goa', 'India', 'Beach', 'October to March', 'https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=900&q=80', 'Golden beaches, forts, nightlife and relaxed coastal stays.'),
('Kerala', 'India', 'Nature', 'September to March', 'https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?auto=format&fit=crop&w=900&q=80', 'Backwaters, hill stations, houseboats and calm green escapes.'),
('Dubai', 'UAE', 'International', 'November to February', 'https://images.unsplash.com/photo-1512453979798-5ea266f8880c?auto=format&fit=crop&w=900&q=80', 'Luxury shopping, desert safaris, skyscrapers and marina experiences.'),
('Jaipur', 'India', 'Heritage', 'October to March', 'https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=900&q=80', 'Royal forts, palaces, markets and colourful Rajasthani culture.'),
('Paris', 'France', 'International', 'April to June', 'https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=900&q=80', 'Museums, river cruises, architecture and romantic city walks.');

INSERT INTO tour_packages (title, destination, category, duration_days, price, available_seats, season_offer, image_url, itinerary) VALUES
('Goa Beach Escape', 'Goa', 'Domestic', 4, 14999.00, 25, '10% summer discount', 'https://images.unsplash.com/photo-1512343879784-a960bf40e7f2?auto=format&fit=crop&w=900&q=80', 'Day 1 arrival, Day 2 North Goa, Day 3 beach activities, Day 4 departure.'),
('Kerala Backwater Bliss', 'Kerala', 'Domestic', 5, 21999.00, 18, 'Free houseboat dinner', 'https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?auto=format&fit=crop&w=900&q=80', 'Cochin, Munnar, Alleppey houseboat and local sightseeing.'),
('Dubai Luxury Tour', 'Dubai', 'International', 6, 74999.00, 12, 'Desert safari included', 'https://images.unsplash.com/photo-1512453979798-5ea266f8880c?auto=format&fit=crop&w=900&q=80', 'City tour, Burj Khalifa, desert safari, marina cruise and shopping.'),
('Himalayan Adventure', 'Manali', 'Domestic', 5, 18999.00, 20, 'Adventure combo included', 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=900&q=80', 'Solang Valley, local temples, trekking, rafting and camp night.'),
('Jaipur Royal Heritage', 'Jaipur', 'Domestic', 3, 11999.00, 30, 'Free heritage walk', 'https://images.unsplash.com/photo-1599661046289-e31897846e41?auto=format&fit=crop&w=900&q=80', 'Amber Fort, City Palace, Hawa Mahal, local bazaar and Rajasthani dinner.'),
('Kashmir Paradise Tour', 'Srinagar', 'Domestic', 6, 32999.00, 14, 'Shikara ride included', 'https://images.unsplash.com/photo-1598091383021-15ddea10925d?auto=format&fit=crop&w=900&q=80', 'Srinagar, Gulmarg, Pahalgam, Dal Lake stay and mountain sightseeing.'),
('Andaman Island Holiday', 'Port Blair', 'Domestic', 5, 38999.00, 16, 'Free snorkeling session', 'https://images.unsplash.com/photo-1559827260-dc66d52bef19?auto=format&fit=crop&w=900&q=80', 'Cellular Jail, Havelock Island, Radhanagar Beach and water activities.'),
('Singapore Family Fun', 'Singapore', 'International', 5, 68999.00, 18, 'Universal Studios pass', 'https://images.unsplash.com/photo-1525625293386-3f8f99389edd?auto=format&fit=crop&w=900&q=80', 'Sentosa, Gardens by the Bay, city tour, night safari and shopping.'),
('Thailand Island Hopper', 'Phuket', 'International', 6, 55999.00, 22, 'Phi Phi island tour', 'https://images.unsplash.com/photo-1508009603885-50cf7c579365?auto=format&fit=crop&w=900&q=80', 'Phuket beaches, island cruise, local markets and cultural evening.'),
('Paris Romance Escape', 'Paris', 'International', 7, 139999.00, 10, 'Seine cruise included', 'https://images.unsplash.com/photo-1502602898657-3e91760cbb34?auto=format&fit=crop&w=900&q=80', 'Eiffel Tower, Louvre, Versailles, Seine cruise and leisure shopping.');

INSERT INTO hotels (name, city, rooms_available, price_per_night, room_type, rating, image_url, description) VALUES
('Sea View Resort', 'Goa', 16, 4500.00, 'Deluxe', 4.5, 'https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=1200&q=80', 'Beach-side resort with pool, restaurant and airport pickup.'),
('Palm Coast Inn', 'Goa', 22, 3200.00, 'Standard', 4.1, 'https://images.unsplash.com/photo-1582719508461-905c673771fd?auto=format&fit=crop&w=1200&q=80', 'Budget-friendly stay near popular beaches and cafes.'),
('Green Valley Stay', 'Kerala', 10, 3800.00, 'Deluxe', 4.3, 'https://images.unsplash.com/photo-1596176530529-78163a4f7af2?auto=format&fit=crop&w=1200&q=80', 'Hill-view rooms near tea gardens with breakfast.'),
('Backwater Pearl', 'Kerala', 12, 5200.00, 'Premium', 4.6, 'https://images.unsplash.com/photo-1596176530529-78163a4f7af2?auto=format&fit=crop&w=1200&q=80', 'Premium stay with houseboat booking and lake-view dining.'),
('Marina Grand Hotel', 'Dubai', 8, 9200.00, 'Suite', 4.8, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80', 'Premium hotel near Dubai Marina with city tour assistance.'),
('Desert Crown Suites', 'Dubai', 15, 7800.00, 'Suite', 4.4, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?auto=format&fit=crop&w=1200&q=80', 'Modern suites with desert safari and airport transfer support.'),
('Snow Peak Inn', 'Manali', 14, 3200.00, 'Standard', 4.2, 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?auto=format&fit=crop&w=1200&q=80', 'Cozy mountain hotel with check-in and check-out support.'),
('Royal Haveli Palace', 'Jaipur', 18, 4100.00, 'Family', 4.5, 'https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=1200&q=80', 'Heritage-style rooms close to Jaipur landmarks.'),
('Dal Lake Retreat', 'Srinagar', 9, 6200.00, 'Premium', 4.7, 'https://images.unsplash.com/photo-1564501049412-61c2a3083791?auto=format&fit=crop&w=1200&q=80', 'Lake-facing hotel with shikara and local sightseeing support.'),
('Island Blue Resort', 'Port Blair', 11, 5900.00, 'Deluxe', 4.4, 'https://images.unsplash.com/photo-1571896349842-33c89424de2d?auto=format&fit=crop&w=1200&q=80', 'Island resort with ferry coordination and beach transfers.'),
('Orchard City Hotel', 'Singapore', 20, 11200.00, 'Family', 4.6, 'https://images.unsplash.com/photo-1445019980597-93fa8acb246c?auto=format&fit=crop&w=1200&q=80', 'Central city hotel near metro, shopping and family attractions.'),
('Patong Bay Resort', 'Phuket', 17, 6500.00, 'Deluxe', 4.3, 'https://images.unsplash.com/photo-1568084680786-a84f91d1153c?auto=format&fit=crop&w=1200&q=80', 'Beach resort with island tour desk and breakfast.'),
('Eiffel View Stay', 'Paris', 7, 14800.00, 'Premium', 4.8, 'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?auto=format&fit=crop&w=1200&q=80', 'Boutique hotel with city passes and Seine cruise help.');

INSERT INTO hotel_room_types (hotel_id, type_name, rooms_available, price_per_night) VALUES
(1, 'Standard', 6, 3600.00), (1, 'Deluxe', 7, 4500.00), (1, 'Suite', 3, 7200.00),
(2, 'Standard', 14, 3200.00), (2, 'Deluxe', 6, 4200.00), (2, 'Family', 2, 5400.00),
(3, 'Standard', 4, 3100.00), (3, 'Deluxe', 4, 3800.00), (3, 'Premium', 2, 5700.00),
(4, 'Deluxe', 5, 4400.00), (4, 'Premium', 5, 5200.00), (4, 'Suite', 2, 7600.00),
(5, 'Deluxe', 3, 7600.00), (5, 'Suite', 4, 9200.00), (5, 'Presidential Suite', 1, 14800.00),
(6, 'Deluxe', 6, 6200.00), (6, 'Suite', 6, 7800.00), (6, 'Family', 3, 8900.00),
(7, 'Standard', 8, 3200.00), (7, 'Deluxe', 4, 4300.00), (7, 'Family', 2, 5100.00),
(8, 'Standard', 7, 3500.00), (8, 'Family', 8, 4100.00), (8, 'Suite', 3, 6400.00),
(9, 'Deluxe', 3, 5100.00), (9, 'Premium', 4, 6200.00), (9, 'Suite', 2, 8300.00),
(10, 'Standard', 4, 4700.00), (10, 'Deluxe', 5, 5900.00), (10, 'Premium', 2, 7600.00),
(11, 'Standard', 8, 8900.00), (11, 'Family', 8, 11200.00), (11, 'Suite', 4, 15400.00),
(12, 'Standard', 7, 5200.00), (12, 'Deluxe', 7, 6500.00), (12, 'Premium', 3, 8400.00),
(13, 'Deluxe', 2, 11200.00), (13, 'Premium', 4, 14800.00), (13, 'Suite', 1, 21000.00);

INSERT INTO hotel_reviews (hotel_id, author_name, rating, comment, created_at) VALUES
(1, 'Aarav Mehta', 5, 'Smooth check-in, clean rooms and the travel desk helped us plan local sightseeing.', NOW()),
(1, 'Priya Nair', 4, 'Comfortable stay with friendly staff. The location made our trip much easier.', NOW()),
(1, 'Rohan Kapoor', 5, 'Good value for the price and the room matched the photos shown online.', NOW());

INSERT INTO bookings (user_id, tour_package_id, hotel_id, travel_date, travellers, total_amount, status) VALUES
(2, 1, 1, '2026-08-20', 2, 29998.00, 'PAID');

INSERT INTO payments (booking_id, amount, transaction_id, gateway_provider, gateway_order_id, gateway_payment_id, paid_at, status) VALUES
(1, 29998.00, 'TXN-DEMO1234', 'RAZORPAY', 'DEMO-ORDER-booking-1', 'TXN-DEMO1234', NOW(), 'SUCCESS');

INSERT INTO feedback (user_id, rating, subject, message, created_at) VALUES
(2, 5, 'Great package options', 'The tourism portal is easy to use and package details are clear.', NOW());
