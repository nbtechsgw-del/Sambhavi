package com.tourism.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigrationRunner implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        migrateBookingsTable();
        migratePaymentsTable();
        migrateHotelsTable();
        migrateUsersTable();
    }

    private void migrateBookingsTable() {
        try {
            jdbcTemplate.execute("ALTER TABLE bookings MODIFY status ENUM('PENDING', 'PAID', 'APPROVED', 'CANCELLED') NOT NULL DEFAULT 'PENDING'");
        } catch (Exception ignored) {
        }
    }

    private void migratePaymentsTable() {
        try {
            jdbcTemplate.execute("ALTER TABLE payments MODIFY transaction_id VARCHAR(100) NULL");
            jdbcTemplate.execute("ALTER TABLE payments MODIFY status ENUM('PENDING', 'SUCCESS', 'REFUNDED', 'FAILED') NOT NULL DEFAULT 'PENDING'");
            addColumnIfMissing("gateway_provider", "ALTER TABLE payments ADD COLUMN gateway_provider VARCHAR(40)");
            addColumnIfMissing("gateway_order_id", "ALTER TABLE payments ADD COLUMN gateway_order_id VARCHAR(100)");
            addColumnIfMissing("gateway_payment_id", "ALTER TABLE payments ADD COLUMN gateway_payment_id VARCHAR(100)");
            addColumnIfMissing("gateway_signature", "ALTER TABLE payments ADD COLUMN gateway_signature VARCHAR(255)");
            addColumnIfMissing("refund_id", "ALTER TABLE payments ADD COLUMN refund_id VARCHAR(100)");
        } catch (Exception ignored) {
        }
    }

    private void addColumnIfMissing(String columnName, String alterSql) {
        addColumnIfMissing("payments", columnName, alterSql);
    }

    private void migrateHotelsTable() {
        try {
            addColumnIfMissing("hotels", "room_type", "ALTER TABLE hotels ADD COLUMN room_type VARCHAR(80) NOT NULL DEFAULT 'Standard'");
            createTableIfMissing("hotel_room_types", """
                    CREATE TABLE hotel_room_types (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        hotel_id BIGINT NOT NULL,
                        type_name VARCHAR(80) NOT NULL,
                        rooms_available INT NOT NULL,
                        price_per_night DECIMAL(10, 2) NOT NULL,
                        CONSTRAINT fk_room_type_hotel FOREIGN KEY (hotel_id) REFERENCES hotels(id)
                    )
                    """);
            jdbcTemplate.execute("""
                    INSERT INTO hotel_room_types (hotel_id, type_name, rooms_available, price_per_night)
                    SELECT h.id, COALESCE(NULLIF(h.room_type, ''), 'Standard'), h.rooms_available, h.price_per_night
                    FROM hotels h
                    WHERE NOT EXISTS (
                        SELECT 1 FROM hotel_room_types rt WHERE rt.hotel_id = h.id
                    )
                    """);
            addColumnIfMissing("hotel_bookings", "room_type_name", "ALTER TABLE hotel_bookings ADD COLUMN room_type_name VARCHAR(80)");
            addColumnIfMissing("hotel_bookings", "room_price_per_night", "ALTER TABLE hotel_bookings ADD COLUMN room_price_per_night DECIMAL(10, 2)");
        } catch (Exception ignored) {
        }
    }

    private void migrateUsersTable() {
        try {
            addColumnIfMissing("users", "personal_code", "ALTER TABLE users ADD COLUMN personal_code VARCHAR(100)");
            jdbcTemplate.execute("UPDATE users SET personal_code = 'ADMIN2026' WHERE role = 'ADMIN' AND (personal_code IS NULL OR personal_code = '')");
        } catch (Exception ignored) {
        }
    }

    private void addColumnIfMissing(String tableName, String columnName, String alterSql) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ? AND COLUMN_NAME = ?",
                Integer.class,
                tableName,
                columnName
        );
        if (count != null && count == 0) {
            jdbcTemplate.execute(alterSql);
        }
    }

    private void createTableIfMissing(String tableName, String createSql) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?",
                Integer.class,
                tableName
        );
        if (count != null && count == 0) {
            jdbcTemplate.execute(createSql);
        }
    }
}
