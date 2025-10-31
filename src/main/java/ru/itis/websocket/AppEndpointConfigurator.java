package ru.itis.websocket;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.ServerEndpointConfig;

public class AppEndpointConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        HttpSession http = (HttpSession) request.getHttpSession();
        if (http != null) {
            config.getUserProperties().put("httpSession", http);
            ServletContext ctx = http.getServletContext();
            config.getUserProperties().put("servletContext", ctx);
        }
    }
}
