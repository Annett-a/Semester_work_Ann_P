package ru.itis.servlets.listing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.repository.PhotoRepository;
import ru.itis.repository.TagRepository;
import ru.itis.service.ListingService;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet("/listings/edit")
public class ListingEditServlet extends HttpServlet {
    private ListingService svc;
    private TagRepository tagRepo;
    private PhotoRepository photoRepo;

    @Override public void init() throws ServletException {
        this.svc = (ListingService) getServletContext().getAttribute("listingService");
        this.tagRepo = (TagRepository) getServletContext().getAttribute("tagRepo");
        this.photoRepo = (PhotoRepository) getServletContext().getAttribute("photoRepo");
        if (svc == null || tagRepo == null || photoRepo == null) throw new ServletException("services not initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        var it = svc.byId(id).orElse(null);
        if (it == null) { resp.sendError(HttpServletResponse.SC_NOT_FOUND); return; }

        req.setAttribute("it", it);
        req.setAttribute("allTags", tagRepo.findAll());
        Set<Long> selected = tagRepo.findByListingId(id).stream()
                .map(t -> t.getId()).collect(Collectors.toSet());
        req.setAttribute("selectedTagIds", selected);

        // ФОТО — список миниатюр
        req.setAttribute("photos", photoRepo.findByListingId(id));

        req.getRequestDispatcher("/jsp/listing-form.jsp").forward(req, resp);
    }
}
