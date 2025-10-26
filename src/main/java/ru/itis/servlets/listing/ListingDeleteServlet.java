package ru.itis.servlets.listing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.itis.service.ListingService;

import java.io.IOException;

@WebServlet("/listings/delete")
public class ListingDeleteServlet extends HttpServlet {
    private ListingService svc;

    @Override
    public void init() throws ServletException {
        this.svc = (ListingService) getServletContext().getAttribute("listingService");
        if (this.svc == null) throw new ServletException("listingService is null");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return;
        }

        Long userId = (Long) s.getAttribute("userId");   // ← берём ID пользователя
        Long id = Long.valueOf(req.getParameter("id"));

        try {
            svc.deleteOwn(id, userId);                   // ← передаём userId, а не email
            resp.sendRedirect(req.getContextPath() + "/listings/my");
        } catch (SecurityException se) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
