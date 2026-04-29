# Online Examination System

Web-based Online Examination System for managing exams, questions, students, attempts, results, and admin activities.

## Objectives

- Automate the examination process.
- Reduce manual effort and paperwork.
- Provide instant results and feedback.
- Ensure secure and fair examinations.
- Manage student and admin activities efficiently.

## Tools And Technologies

- Frontend: HTML, CSS, JavaScript
- Backend: Java
- Database: MySQL
- Platform: Web-based system

## System Modules

### Admin Module

- Add and manage questions.
- Create exams.
- Set exam time limits.
- View student results.

### User Module

- Register and login.
- Attempt exam.
- View instant results.

## Project Structure

```text
Online Exam/
  frontend/
    index.html
    styles.css
    app.js
  database/
    schema.sql
    seed.sql
  backend/
    pom.xml
    src/main/java/com/onlineexam/
      model/
      dao/
      servlet/
  docs/
    project-report.md
```

## Run The Prototype

Open `frontend/index.html` in a browser.

## Run The Java Backend

Run Maven from the `backend/` directory, not the project root.

```bash
cd backend
mvn clean package
```

If you want to start the servlet container through Maven, use:

```bash
cd backend
mvn jetty:run
```

This project now keeps Maven's local repository inside `backend/.m2/`, which avoids the local repository access issue that can happen in restricted environments.

Demo accounts:

- Admin: `admin@exam.com` / `admin123`
- Student: `student@exam.com` / `student123`

The frontend prototype uses browser local storage, so it works without a server. The Java and MySQL files are included as a backend starter for full implementation.
