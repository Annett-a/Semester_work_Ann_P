package ru.itis.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/profile", "/listings/*", "/chat"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        var r = (HttpServletRequest) req;
        var s = r.getSession(false);
        boolean loggedIn = s != null && s.getAttribute("userId") != null;
        if (loggedIn) {
            chain.doFilter(req, resp);
        } else {
            ((HttpServletResponse) resp).sendRedirect(r.getContextPath() + "/sign-in");
        }
    }
}
