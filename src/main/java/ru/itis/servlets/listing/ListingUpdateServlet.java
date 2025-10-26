package ru.itis.servlets.listing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.model.ListingEntity;
import ru.itis.service.ListingService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet("/listings/update")
public class ListingUpdateServlet extends HttpServlet {
    private ListingService svc;
    @Override public void init() { this.svc = (ListingService) getServletContext().getAttribute("listingService"); }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession s = req.getSession(false);
        Long userId = (Long) s.getAttribute("userId");

        ListingEntity e = new ListingEntity();
        e.setId(Long.valueOf(req.getParameter("id")));
        e.setTitle(req.getParameter("title"));
        e.setType(req.getParameter("type"));
        e.setStatus(req.getParameter("status"));

        List<Long> tagIds = parseTagIds(req.getParameterValues("tagIds"));

        try {
            svc.updateOwn(e, userId, tagIds);
            resp.sendRedirect(req.getContextPath() + "/listings/view?id=" + e.getId());
        } catch (SecurityException se) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (IllegalArgumentException iae) {
            s.setAttribute("errors", Map.of("common", "Заполните поля"));
            s.setAttribute("form", Map.of(
                    "id", e.getId().toString(),
                    "title", e.getTitle(), "type", e.getType(), "status", e.getStatus()
            ));
            resp.sendRedirect(req.getContextPath() + "/listings/edit?id=" + e.getId());
        }
    }

    private List<Long> parseTagIds(String[] raw) {
        if (raw == null || raw.length == 0) return Collections.emptyList();
        return Stream.of(raw).map(Long::valueOf).collect(Collectors.toList());
    }
}
