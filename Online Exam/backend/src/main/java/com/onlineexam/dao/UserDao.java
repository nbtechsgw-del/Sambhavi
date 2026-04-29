package com.onlineexam.dao;

import com.onlineexam.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDao {
    public Optional<User> findByCredentials(String email, String passwordHash) throws SQLException {
        String sql = "SELECT id, name, email, role FROM online_exam_users WHERE email = ? AND password_hash = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, passwordHash);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("role")
                    ));
                }
            }
        }

        return Optional.empty();
    }
}
