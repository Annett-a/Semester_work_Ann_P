package ru.itis.websocket;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import ru.itis.service.RoomChatService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/ws/chat", configurator = AppEndpointConfigurator.class)
public class ChatWebSocketEndpoint {
    private static final ConcurrentMap<Long, CopyOnWriteArraySet<Session>> ROOMS = new ConcurrentHashMap<>();
    private static volatile RoomChatService chatService;

    private static RoomChatService chat(ServletContext ctx) {
        if (chatService == null) {
            synchronized (ChatWebSocketEndpoint.class) {
                if (chatService == null) {
                    chatService = (RoomChatService) ctx.getAttribute("chatService");
                    if (chatService == null) throw new IllegalStateException("chatService not initialized");
                }
            }
        }
        return chatService;
    }

    private static Long parseRoomId(Session ws) {
        String q = ws.getRequestURI().getQuery();
        if (q == null) return null;
        for (String p : q.split("&")) {
            int i = p.indexOf('=');
            String k = i>0 ? p.substring(0,i) : p;
            String v = i>0 ? p.substring(i+1) : "";
            if ("listing".equals(k)) {
                try { return Long.valueOf(v); } catch (Exception ignored) {}
            }
        }
        return null;
    }

    @OnOpen
    public void onOpen(Session ws, EndpointConfig config) throws IOException {
        HttpSession http = (HttpSession) config.getUserProperties().get("httpSession");
        ServletContext ctx = (ServletContext) config.getUserProperties().get("servletContext");
        if (http == null || ctx == null) {
            ws.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "No HTTP/Servlet context"));
            return;
        }
        ws.getUserProperties().put("httpSession", http);
        ws.getUserProperties().put("servletContext", ctx);

        Long roomId = parseRoomId(ws);
        if (roomId == null) {
            ws.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "listing param required"));
            return;
        }

        ROOMS.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>()).add(ws);
    }

    @OnMessage
    public void onMessage(Session ws, String payload) {
        Long roomId = parseRoomId(ws);
        if (roomId == null || payload == null || payload.isBlank()) return;

        HttpSession http = (HttpSession) ws.getUserProperties().get("httpSession");
        ServletContext ctx = (ServletContext) ws.getUserProperties().get("servletContext");

        String email = http != null ? (String) http.getAttribute("email") : null;
        String who = (email != null && !email.isBlank()) ? email : "anonymous";
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String line = "[" + ts + "] " + who + ": " + payload.trim();

        if (ctx != null) {
            try { chat(ctx).add(roomId, line); } catch (Exception ignored) {}
        }
        broadcast(roomId, line);
    }

    @OnClose
    public void onClose(Session ws, CloseReason reason) {
        Long roomId = parseRoomId(ws);
        if (roomId == null) return;
        Set<Session> set = ROOMS.get(roomId);
        if (set != null) set.remove(ws);
        if (set != null && set.isEmpty()) ROOMS.remove(roomId);
    }

    @OnError
    public void onError(Session ws, Throwable t) {
        onClose(ws, null);
    }

    private static void broadcast(Long room, String msg) {
        var set = ROOMS.get(room);
        if (set == null) return;
        for (Session s : set) {
            try { s.getBasicRemote().sendText(msg); }
            catch (IOException ignored) {}
        }
    }
}
