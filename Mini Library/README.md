# Mini Library Management System

A simple Java console application for managing books, users, and borrowing records.

## Features

- Add new books
- View all books
- Search books by title or author
- Track book availability
- Register users with unique IDs
- View all users
- Borrow books if available
- Return borrowed books
- Prevent duplicate borrowing
- Track which user borrowed which book

## How to Run

1. Open a terminal in this project folder.
2. Compile the program:

```bash
javac Main.java
```

3. Run it:

```bash
java -cp . Main
```

## Data Storage

The app stores all data in `library_data.dat`, so your books and users stay saved between runs.
