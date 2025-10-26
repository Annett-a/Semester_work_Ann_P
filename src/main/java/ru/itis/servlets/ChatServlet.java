package ru.itis.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.service.RoomChatService;

import java.io.IOException;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    private RoomChatService chat;

    @Override
    public void init() throws ServletException {
        chat = (RoomChatService) getServletContext().getAttribute("chatService");
        if (chat == null) throw new ServletException("chatService is null");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String raw = req.getParameter("listing");
        if (raw == null) { resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "listing required"); return; }
        Long listingId;
        try { listingId = Long.valueOf(raw); } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "bad listing id"); return;
        }
        req.setAttribute("roomId", listingId);
        req.setAttribute("messages", chat.last(listingId, 20));
        req.getRequestDispatcher("/jsp/chat.jsp").forward(req, resp);
    }
}
