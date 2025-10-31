package ru.itis.servlets.listing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.service.PhotoService;
import java.io.IOException;

@WebServlet("/listings/photos/upload")
@MultipartConfig( maxFileSize = 5 * 1024 * 1024, maxRequestSize = 30 * 1024 * 1024 )
public class ListingPhotoUploadServlet extends HttpServlet {
    private PhotoService photos;

    @Override public void init() throws ServletException {
        this.photos = (PhotoService) getServletContext().getAttribute("photoService");
        if (photos == null) throw new ServletException("photoService is null");
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
        Long listingId = Long.valueOf(req.getParameter("listingId"));
        var parts = req.getParts().stream()
                .filter(p -> "photos".equals(p.getName()))
                .toList();
        photos.upload(listingId, userId, parts);
        resp.sendRedirect(req.getContextPath() + "/listings/edit?id=" + listingId);
    }
}
