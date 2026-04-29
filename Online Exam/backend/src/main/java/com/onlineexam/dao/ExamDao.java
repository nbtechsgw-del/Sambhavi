package com.onlineexam.dao;

import com.onlineexam.model.Exam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExamDao {
    public List<Exam> findActiveExams() throws SQLException {
        String sql = "SELECT id, title, duration_minutes, status FROM online_exam_exams WHERE status = 'ACTIVE' ORDER BY id DESC";
        List<Exam> exams = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                exams.add(new Exam(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getInt("duration_minutes"),
                    resultSet.getString("status")
                ));
            }
        }

        return exams;
    }
}
