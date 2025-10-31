package ru.itis.servlets.listing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.model.ListingEntity;
import ru.itis.service.ListingService;
import ru.itis.service.PhotoService;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet("/listings/create")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024, maxRequestSize = 30 * 1024 * 1024)
public class ListingCreateServlet extends HttpServlet {
    private ListingService svc;
    private PhotoService photos;

    @Override
    public void init() throws ServletException {
        this.svc = (ListingService) getServletContext().getAttribute("listingService");
        this.photos = (PhotoService) getServletContext().getAttribute("photoService");
        if (svc == null || photos == null) throw new ServletException("services not initialized");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");

        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/sign-in");
            return;
        }
        Long userId = (Long) s.getAttribute("userId");
        ListingEntity e = new ListingEntity();
        e.setTitle(req.getParameter("title"));
        e.setType(req.getParameter("type"));
        e.setStatus(req.getParameter("status"));
        e.setAuthorId(userId);

        List<Long> tagIds = parseTagIds(req.getParameterValues("tagIds"));
        Long id;
        try {
            id = svc.create(e, tagIds);
        } catch (IllegalArgumentException ex) {
            s.setAttribute("errors", Map.of("common", "Заполните поля корректно"));
            s.setAttribute("form", Map.of(
                    "title", e.getTitle(),
                    "type", e.getType(),
                    "status", e.getStatus()
            ));
            resp.sendRedirect(req.getContextPath() + "/listings/new");
            return;
        }
        var parts = req.getParts().stream()
                .filter(p -> "photos".equals(p.getName()))
                .collect(Collectors.toList());
        if (!parts.isEmpty()) {
            try {
                photos.upload(id, userId, parts);
            } catch (Exception ex) {
                s.setAttribute("errors", Map.of("flash", "Объявление создано, но фото не загрузились"));
            }
        }
        resp.sendRedirect(req.getContextPath() + "/listings/view?id=" + id);
    }

    private static List<Long> parseTagIds(String[] raw) {
        if (raw == null || raw.length == 0) return Collections.emptyList();
        return Stream.of(raw).map(Long::valueOf).collect(Collectors.toList());
    }
}
