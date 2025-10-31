package ru.itis.servlets.listing;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.service.PhotoService;

@WebServlet("/listings/photos/delete")
public class ListingPhotoDeleteServlet extends HttpServlet {
    private PhotoService photos;

    @Override public void init() {
        this.photos = (PhotoService) getServletContext().getAttribute("photoService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws java.io.IOException {
        Long photoId = Long.valueOf(req.getParameter("photoId"));
        Long listingId = Long.valueOf(req.getParameter("listingId"));
        Long userId = (Long) req.getSession(false).getAttribute("userId");
        try {
            photos.delete(photoId, userId);
            resp.sendRedirect(req.getContextPath() + "/listings/edit?id=" + listingId);
        } catch (SecurityException se) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
