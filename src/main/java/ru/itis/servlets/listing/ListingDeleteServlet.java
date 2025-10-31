package ru.itis.servlets.listing;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.service.ListingService;
import java.io.IOException;

@WebServlet("/listings/delete")
public class ListingDeleteServlet extends HttpServlet {
    private ListingService svc;

    @Override
    public void init() {
        this.svc = (ListingService) getServletContext().getAttribute("listingService");
        if (svc == null) throw new IllegalStateException("listingService not found in context");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return;
        }
        Long userId = (Long) s.getAttribute("userId");
        Long id;
        try {
            id = Long.valueOf(req.getParameter("id"));
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный id");
            return;
        }
        try {
            svc.deleteOwn(id, userId);
        } catch (SecurityException se) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String back = req.getParameter("back");
        String ctx  = req.getContextPath();
        if ("all".equals(back)) {
            resp.sendRedirect(ctx + "/listings");
        } else if ("my".equals(back)) {
            resp.sendRedirect(ctx + "/listings/my");
        } else {
            String ref = req.getHeader("referer");
            if (ref != null && (ref.contains("/listings"))) {
                resp.sendRedirect(ref);
            } else {
                resp.sendRedirect(ctx + "/listings");
            }
        }
    }
}
