package ru.itis.service.impl;

import jakarta.servlet.http.Part;
import ru.itis.config.FileStorageConfig;
import ru.itis.model.PhotoEntity;
import ru.itis.repository.ListingRepository;
import ru.itis.repository.PhotoRepository;
import ru.itis.service.PhotoService;

import java.io.*;
import java.util.*;

public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photos;
    private final ListingRepository listings;

    public PhotoServiceImpl(PhotoRepository photos, ListingRepository listings) {
        this.photos = photos;
        this.listings = listings;
    }

    @Override
    public List<Long> upload(Long listingId, Long userId, List<Part> parts) {
        if (!listings.isOwner(listingId, userId)) {
            throw new SecurityException("Not owner");
        }
        if (parts == null) return Collections.emptyList();

        File base = FileStorageConfig.baseDir();
        List<Long> ids = new ArrayList<>();

        for (Part p : parts) {
            String ct = p.getContentType() == null ? "" : p.getContentType();
            long size = p.getSize();
            if (size <= 0 || !ct.startsWith("image/")) continue;

            String original = p.getSubmittedFileName();
            if (original == null || original.isBlank()) original = "upload.bin";

            String ext = "";
            int dot = original.lastIndexOf('.');
            if (dot > -1 && dot < original.length() - 1) ext = original.substring(dot);

            String uuid = UUID.randomUUID().toString().replace("-", "");
            String relPath = "listing" + File.separator + listingId + File.separator + uuid + ext;
            File dest = new File(base, relPath).getAbsoluteFile();

            File parent = dest.getParentFile();
            if (!parent.exists() && !parent.mkdirs()) {
                throw new RuntimeException("Cannot create dir: " + parent);
            }

            try (InputStream in = p.getInputStream();
                 OutputStream out = new FileOutputStream(dest)) {
                copy(in, out);
            } catch (IOException ex) {
                throw new RuntimeException("store file failed", ex);
            }

            PhotoEntity e = new PhotoEntity();
            e.setListingId(listingId);
            e.setFileName(original);
            // в БД храним относительный путь с прямыми слэшами — удобнее для URL
            e.setStoragePath(relPath.replace(File.separatorChar, '/'));
            e.setContentType(ct);
            e.setSize(size);

            Long id = photos.create(e);
            if (id != null) ids.add(id);
        }
        return ids;
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[8192];
        int r;
        while ((r = in.read(buf)) != -1) {
            out.write(buf, 0, r);
        }
    }

    @Override
    public Optional<PhotoEntity> findById(Long id) {
        return photos.findById(id);
    }

    @Override
    public List<PhotoEntity> byListing(Long listingId) {
        return photos.findByListingId(listingId);
    }

    @Override
    public void delete(Long photoId, Long userId) {
        PhotoEntity p = photos.findById(photoId).orElseThrow();
        if (!listings.isOwner(p.getListingId(), userId)) {
            throw new SecurityException("Not owner");
        }
        File file = new File(FileStorageConfig.baseDir(), p.getStoragePath().replace('/', File.separatorChar));
        // молча игнорим ошибку удаления файла — запись в БД всё равно удаляем
        if (file.exists() && !file.delete()) {
            // можно залогировать
        }
        photos.delete(photoId);
    }
}
