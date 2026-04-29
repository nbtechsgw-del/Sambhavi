package com.onlineexam.servlet;

import com.google.gson.Gson;
import com.onlineexam.dao.UserDao;
import com.onlineexam.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {
    private final Gson gson = new Gson();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        applyCors(response);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        applyCors(response);
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(Map.of(
            "message", "Use POST to log in",
            "endpoint", "/api/login",
            "requiredFields", new String[] {"email", "password"}
        )));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        applyCors(response);
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        response.setContentType("application/json");

        try {
            Optional<User> user = userDao.findByCredentials(email, password);
            if (user.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write(gson.toJson(Map.of("message", "Invalid email or password")));
                return;
            }

            request.getSession().setAttribute("user", user.get());
            response.getWriter().write(gson.toJson(user.get()));
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
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }
}
