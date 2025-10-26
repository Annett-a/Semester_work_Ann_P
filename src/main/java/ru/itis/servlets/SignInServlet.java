package ru.itis.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.model.UserEntity;
import ru.itis.repository.UserRepository;
import ru.itis.service.AuthService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/sign-in")
public class SignInServlet extends HttpServlet {
    private AuthService auth;
    private UserRepository users;

    @Override
    public void init() throws ServletException {
        this.auth = (AuthService) getServletContext().getAttribute("authService");
        this.users = (UserRepository) getServletContext().getAttribute("userRepo");
        if (auth == null || users == null) throw new ServletException("Services not initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/sign-in.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Map<String,String> errors = new HashMap<>();
        if (email == null || email.isBlank()) errors.put("email", "E-mail обязателен");
        if (password == null || password.isBlank()) errors.put("password", "Пароль обязателен");

        if (!errors.isEmpty()) {
            var s = req.getSession(true);
            s.setAttribute("errors", errors);
            s.setAttribute("form", Map.of("email", email));
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return;
        }

        if (auth.authenticate(email, password)) {
            UserEntity u = users.findByEmail(email).orElseThrow();
            var s = req.getSession(true);
            s.setAttribute("email", email);
            s.setAttribute("userId", u.getId()); // ← храним id в сессии (O2M)
            resp.sendRedirect(req.getContextPath() + "/profile");
        } else {
            var s = req.getSession(true);
            s.setAttribute("errors", Map.of("common", "Неверные e-mail или пароль"));
            s.setAttribute("form", Map.of("email", email));
            resp.sendRedirect(req.getContextPath() + "/sign-in");
        }
    }
}
