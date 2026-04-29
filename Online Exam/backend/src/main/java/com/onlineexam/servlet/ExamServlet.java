package com.onlineexam.servlet;

import com.google.gson.Gson;
import com.onlineexam.dao.ExamDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/api/exams")
public class ExamServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final ExamDao examDao = new ExamDao();

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        applyCors(response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        applyCors(response);
        response.setContentType("application/json");

        try {
            response.getWriter().write(gson.toJson(examDao.findActiveExams()));
        } catch (SQLException exception) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(gson.toJson(Map.of(
                "message", "Database error",
                "details", exception.getMessage()
            )));
        }
    }

    private void applyCors(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
