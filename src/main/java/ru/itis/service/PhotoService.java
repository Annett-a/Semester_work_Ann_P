package ru.itis.service;

import jakarta.servlet.http.Part;
import ru.itis.model.PhotoEntity;

import java.util.List;
import java.util.Optional;

public interface PhotoService {
    List<Long> upload(Long listingId, Long userId, List<Part> parts);
    Optional<PhotoEntity> findById(Long id);
    List<PhotoEntity> byListing(Long listingId);
    void delete(Long photoId, Long userId);
}
