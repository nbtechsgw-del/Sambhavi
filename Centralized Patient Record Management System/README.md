# Centralized Patient Record Management System

This project is a Java Spring Boot backend for a centralized hospital record platform based on the provided requirements document.

## Implemented modules

- Patient registration and searchable patient directory
- Electronic medical records for diagnoses, treatments, allergies, prescriptions, and lab report links
- Appointment scheduling and tracking
- Billing and payment record management
- Role-based secured APIs for admin, doctor, nurse, and receptionist users
- Reporting endpoints for patient, appointment, and billing summaries

## Tech stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- MySQL

## Default users

- `admin / admin123`
- `doctor / doctor123`
- `nurse / nurse123`
- `reception / reception123`

## Run

```bash
mvn spring-boot:run
```

Before running, make sure MySQL is started locally and that the configured username and password in `src/main/resources/application.yml` are correct for your machine.

## Useful endpoints

- `GET /api/patients`
- `POST /api/patients`
- `GET /api/patients/search?keyword=rahul`
- `POST /api/patients/{patientId}/records`
- `GET /api/patients/{patientId}/records`
- `POST /api/appointments`
- `PUT /api/appointments/{appointmentId}/status?status=COMPLETED`
- `POST /api/bills`
- `PUT /api/bills/{billId}/payment?status=PAID`
- `GET /api/reports/dashboard`

## Database

- Database name: `patientrecords`
- Default connection: `jdbc:mysql://localhost:3306/patientrecords`
- Default username: `root`
- Default password: `root`

If your MySQL credentials are different, update `src/main/resources/application.yml`.
