package ru.itis.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class Flash {
    private Flash() {}
        public static final String ERRORS = "FLASH_ERRORS";
        public static final String FORM = "FLASH_FORM";
        public static final String NOTICE = "FLASH_NOTICE";

        public static void put(HttpServletRequest req, String key, Object value){
            req.getSession().setAttribute(key, value);
        }
        @SuppressWarnings("unchecked")
        public static <T> T take(HttpServletRequest req, String key) {
            HttpSession s = req.getSession(false);
            if (s==null) return null;
            Object v = s.getAttribute(key);
            if (v != null) s.removeAttribute(key);
            return(T) v;
    }
}
