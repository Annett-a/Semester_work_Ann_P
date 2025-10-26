package ru.itis.servlets;

import ru.itis.util.CookieSearchUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/cookie")
public class CookieServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> optionalColor = CookieSearchUtil
                .findCookieByName(req.getCookies(), "color");

        req.setAttribute("color", optionalColor.orElse("green"));
        req.getRequestDispatcher("/jsp/cookie.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String color = req.getParameter("color");
        Cookie cookie = new Cookie("color", color);
        cookie.setMaxAge(30);
        resp.addCookie(cookie);
        resp.sendRedirect("/cookie");
    }
}
