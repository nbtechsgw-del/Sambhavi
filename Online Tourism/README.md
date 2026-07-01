# Online Tourism Management System

A Java Spring Boot web application for tourism package booking, hotel management, payments, reports and feedback.

## Tech Stack

- Java 17
- Spring Boot MVC
- Spring Data JPA
- Thymeleaf HTML templates
- CSS and JavaScript
- MySQL

## Setup

1. Create the database:
   ```sql
   CREATE DATABASE tourism_db;
   ```
2. Update MySQL username/password in `src/main/resources/application.properties`, or run with environment variables:
   ```powershell
   $env:DB_USERNAME="root"
   $env:DB_PASSWORD="your_mysql_password"
   mvn spring-boot:run
   ```
3. For Razorpay payments, set keys through environment variables. Do not commit real keys:
   ```powershell
   $env:RAZORPAY_KEY_ID="your_razorpay_key_id"
   $env:RAZORPAY_KEY_SECRET="your_razorpay_key_secret"
   ```
4. Run the project:
   ```bash
   mvn spring-boot:run
   ```
5. Open `http://localhost:8080`.

## Demo Login

- Admin: `admin@tourism.com` / `admin123`
- User: register from the site, or use `rahul@example.com` / `user123` after importing `src/main/resources/db/schema.sql`.

## Main Modules

- Admin dashboard for packages, hotels, users, bookings, payments and feedback.
- User registration/login, package search, bookings, payment records and reviews.
- SQL schema with sample packages, hotels, users, bookings, payments and feedback.
