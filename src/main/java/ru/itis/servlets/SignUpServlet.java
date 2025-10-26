package ru.itis.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.service.AuthService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/sign-up")
public class SignUpServlet extends HttpServlet {
    private AuthService auth;

    @Override
    public void init() { this.auth = (AuthService) getServletContext().getAttribute("authService"); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/sign-up.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");

        Map<String,String> errors = new HashMap<>();
        if (email == null || email.isBlank()) errors.put("email", "E-mail обязателен");
        if (password == null || password.isBlank()) errors.put("password", "Пароль обязателен");

        if (!errors.isEmpty()) {
            var s = req.getSession(true);
            s.setAttribute("errors", errors);
            s.setAttribute("form", Map.of("email", email, "fullName", fullName));
            resp.sendRedirect(req.getContextPath() + "/sign-up");
            return;
        }

        try {
            auth.register(email, password, fullName);
            resp.sendRedirect(req.getContextPath() + "/sign-in");
        } catch (IllegalStateException | IllegalArgumentException ex) {
            var s = req.getSession(true);
            s.setAttribute("errors", Map.of("common", ex.getMessage()));
            s.setAttribute("form", Map.of("email", email, "fullName", fullName));
            resp.sendRedirect(req.getContextPath() + "/sign-up");
        }
    }
}
