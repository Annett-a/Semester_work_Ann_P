package ru.itis.websocket;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/ws/chat",
        configurator = AppEndpointConfigurator.class
)
public class ChatWebSocketEndpoint {

    private static final Set<Session> CLIENTS = ConcurrentHashMap.newKeySet();

    private HttpSession httpSession;
    private ServletContext servletContext;

    @OnOpen
    public void onOpen(Session ws, EndpointConfig config) {
        CLIENTS.add(ws);

        Object hs = config.getUserProperties().get("httpSession");
        if (hs instanceof HttpSession) {
            this.httpSession = (HttpSession) hs;
            this.servletContext = this.httpSession.getServletContext();
        }

        ws.getUserProperties().put("httpSession", this.httpSession);
        ws.getUserProperties().put("servletContext", this.servletContext);
    }

    @OnMessage
    public void onMessage(String text, Session ws) throws IOException {
        for (Session s : CLIENTS) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText(text);
            }
        }
    }

    @OnClose
    public void onClose(Session ws, CloseReason reason) {
        CLIENTS.remove(ws);
    }

    @OnError
    public void onError(Session ws, Throwable t) {
        t.printStackTrace();
    }
}
