# Attendance & Leave Management API

Spring Boot REST API for a corporate attendance system with JWT authentication, role-based access, MySQL persistence, attendance tracking, and leave approval workflows.

## Tech Stack

- Java 21
- Spring Boot 3
- Spring Security + JWT
- Spring Data JPA
- MySQL
- Maven

## Features

- `Admin` and `Employee` roles
- Login with JWT token
- Employee check-in and check-out
- Working hour calculation from check-in/check-out timestamps
- Leave application by employees
- Leave approval/rejection by admins
- Duplicate daily check-in prevention
- Overlapping leave request prevention
- Configurable application timezone for correct date handling

## Default Seeded Users

- Admin: `admin` / `Admin@123`
- Employee: `employee` / `Employee@123`

## MySQL Setup

1. Start MySQL on port `3306`.
2. Create `attendance_db` or let the app create it automatically.
3. Update [application.properties](/c:/Users/shamb/OneDrive/Documents/Internship%20daily%20task/Day%207/src/main/resources/application.properties) if your MySQL username/password differs from `root` / `root`.

## Run

```bash
mvn spring-boot:run
```

## Main Endpoints

- `POST /api/auth/login`
- `POST /api/attendance/check-in`
- `POST /api/attendance/check-out`
- `GET /api/attendance/me`
- `POST /api/leaves`
- `GET /api/leaves/me`
- `POST /api/admin/employees`
- `GET /api/admin/users`
- `GET /api/admin/leaves/pending`
- `POST /api/admin/leaves/{leaveId}/approve`
- `POST /api/admin/leaves/{leaveId}/reject`

## Sample Requests

Login:

```json
{
  "username": "admin",
  "password": "Admin@123"
}
```

Apply leave:

```json
{
  "startDate": "2026-04-20",
  "endDate": "2026-04-22",
  "reason": "Family function"
}
```

Approve or reject leave:

```json
{
  "comment": "Approved for planned leave"
}
```

## Business Rules

- One employee can only check in once per day.
- Check-out requires a same-day check-in.
- Working hours are stored as minutes and returned as `HH:mm`.
- Leave end date cannot be before start date.
- Pending or approved leave requests cannot overlap.
- All date logic uses the configured `app.timezone`.
