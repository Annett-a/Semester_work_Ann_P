package ru.itis.servlets.listing;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.service.ListingService;
import java.io.IOException;

@WebServlet("/listings")
public class ListingsServlet extends HttpServlet {
    private ListingService svc;

    @Override public void init() { this.svc = (ListingService) getServletContext().getAttribute("listingService"); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("items", svc.all());
        req.getRequestDispatcher("/jsp/listings.jsp").forward(req, resp);
    }
}
