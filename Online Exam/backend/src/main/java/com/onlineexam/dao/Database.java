package com.onlineexam.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    private static final String DATABASE_NAME = "mydb";
    private static final String SERVER_URL = "jdbc:mysql://localhost:3306/";
    private static final String URL = SERVER_URL + DATABASE_NAME;
    private static final String USER = "root";
    private static final String PASSWORD = "Root";
    private static final String USERS_TABLE = "online_exam_users";
    private static final String EXAMS_TABLE = "online_exam_exams";
    private static final String QUESTIONS_TABLE = "online_exam_questions";
    private static final String EXAM_QUESTIONS_TABLE = "online_exam_exam_questions";
    private static final String ATTEMPTS_TABLE = "online_exam_attempts";
    private static final String ATTEMPT_ANSWERS_TABLE = "online_exam_attempt_answers";
    private static volatile boolean initialized;

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        ensureDatabaseExists();
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        initializeIfNeeded(connection);
        return connection;
    }

    private static void ensureDatabaseExists() throws SQLException {
        try (Connection connection = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME);
        }
    }

    private static void initializeIfNeeded(Connection connection) throws SQLException {
        if (initialized) {
            return;
        }

        synchronized (Database.class) {
            if (initialized) {
                return;
            }

            createTables(connection);
            seedData(connection);
            initialized = true;
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + USERS_TABLE + " (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(100) NOT NULL," +
                    "email VARCHAR(120) NOT NULL UNIQUE," +
                    "password_hash VARCHAR(255) NOT NULL," +
                    "role ENUM('ADMIN', 'STUDENT') NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + EXAMS_TABLE + " (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "title VARCHAR(150) NOT NULL," +
                    "duration_minutes INT NOT NULL," +
                    "status ENUM('DRAFT', 'ACTIVE', 'CLOSED') NOT NULL DEFAULT 'DRAFT'," +
                    "created_by INT NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (created_by) REFERENCES " + USERS_TABLE + "(id)" +
                ")"
            );

            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + QUESTIONS_TABLE + " (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "question_text TEXT NOT NULL," +
                    "option_a VARCHAR(255) NOT NULL," +
                    "option_b VARCHAR(255) NOT NULL," +
                    "option_c VARCHAR(255) NOT NULL," +
                    "option_d VARCHAR(255) NOT NULL," +
                    "correct_option CHAR(1) NOT NULL," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "CHECK (correct_option IN ('A', 'B', 'C', 'D'))" +
                ")"
            );

            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + EXAM_QUESTIONS_TABLE + " (" +
                    "exam_id INT NOT NULL," +
                    "question_id INT NOT NULL," +
                    "PRIMARY KEY (exam_id, question_id)," +
                    "FOREIGN KEY (exam_id) REFERENCES " + EXAMS_TABLE + "(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (question_id) REFERENCES " + QUESTIONS_TABLE + "(id) ON DELETE CASCADE" +
                ")"
            );

            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + ATTEMPTS_TABLE + " (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "user_id INT NOT NULL," +
                    "exam_id INT NOT NULL," +
                    "score INT NOT NULL DEFAULT 0," +
                    "total_questions INT NOT NULL DEFAULT 0," +
                    "started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "submitted_at TIMESTAMP NULL," +
                    "FOREIGN KEY (user_id) REFERENCES " + USERS_TABLE + "(id)," +
                    "FOREIGN KEY (exam_id) REFERENCES " + EXAMS_TABLE + "(id)" +
                ")"
            );

            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " + ATTEMPT_ANSWERS_TABLE + " (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "attempt_id INT NOT NULL," +
                    "question_id INT NOT NULL," +
                    "selected_option CHAR(1)," +
                    "is_correct BOOLEAN NOT NULL DEFAULT FALSE," +
                    "FOREIGN KEY (attempt_id) REFERENCES " + ATTEMPTS_TABLE + "(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (question_id) REFERENCES " + QUESTIONS_TABLE + "(id)" +
                ")"
            );
        }
    }

    private static void seedData(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + USERS_TABLE)) {
            resultSet.next();
            if (resultSet.getInt(1) > 0) {
                return;
            }
        }

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(
                "INSERT INTO " + USERS_TABLE + " (name, email, password_hash, role) VALUES " +
                    "('System Admin', 'admin@exam.com', 'admin123', 'ADMIN')," +
                    "('Demo Student', 'student@exam.com', 'student123', 'STUDENT')"
            );

            statement.executeUpdate(
                "INSERT INTO " + EXAMS_TABLE + " (title, duration_minutes, status, created_by) VALUES " +
                    "('Java Fundamentals', 10, 'ACTIVE', 1)," +
                    "('Web Technology Basics', 8, 'ACTIVE', 1)"
            );

            statement.executeUpdate(
                "INSERT INTO " + QUESTIONS_TABLE + " (question_text, option_a, option_b, option_c, option_d, correct_option) VALUES " +
                    "('Which keyword is used to inherit a class in Java?', 'implements', 'extends', 'inherits', 'super', 'B')," +
                    "('Which method is the entry point of a Java program?', 'start()', 'main()', 'run()', 'init()', 'B')," +
                    "('Which language is used for styling web pages?', 'HTML', 'CSS', 'SQL', 'Java', 'B')"
            );

            statement.executeUpdate(
                "INSERT INTO " + EXAM_QUESTIONS_TABLE + " (exam_id, question_id) VALUES " +
                    "(1, 1)," +
                    "(1, 2)," +
                    "(2, 3)"
            );
        }
    }
}
