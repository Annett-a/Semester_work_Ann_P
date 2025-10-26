package ru.itis.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ru.itis.config.FileStorageConfig;
import ru.itis.model.PhotoEntity;
import ru.itis.service.PhotoService;

import java.io.*;

@WebServlet("/photos")
public class PhotoContentServlet extends HttpServlet {
    private PhotoService svc;

    @Override public void init() throws ServletException {
        this.svc = (PhotoService) getServletContext().getAttribute("photoService");
        if (svc == null) throw new ServletException("photoService is null");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.valueOf(req.getParameter("id"));
        PhotoEntity p = svc.findById(id).orElse(null);
        if (p == null) { resp.sendError(404); return; }

        File file = new File(FileStorageConfig.baseDir(), p.getStoragePath().replace('/', File.separatorChar));
        if (!file.exists() || !file.isFile()) { resp.sendError(404); return; }

        resp.setContentType(p.getContentType() == null ? "application/octet-stream" : p.getContentType());
        resp.setHeader("Cache-Control", "max-age=31536000, public");
        resp.setContentLengthLong(file.length());

        try (InputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = in.read(buf)) != -1) out.write(buf, 0, r);
        }
    }
}
