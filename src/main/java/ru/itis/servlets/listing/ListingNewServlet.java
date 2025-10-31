package ru.itis.servlets.listing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.repository.TagRepository;
import java.io.IOException;

@WebServlet("/listings/new")
public class ListingNewServlet extends HttpServlet {
    private TagRepository tagRepo;

    @Override
    public void init() throws ServletException {
        this.tagRepo = (TagRepository) getServletContext().getAttribute("tagRepo");
        if (tagRepo == null) throw new ServletException("tagRepo is null");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        req.setAttribute("allTags", tagRepo.findAll());
        req.getRequestDispatcher("/jsp/listing-form.jsp").forward(req, resp);
    }
}
