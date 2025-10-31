package ru.itis.repository;

import ru.itis.model.PhotoEntity;
import java.util.List;
import java.util.Optional;

public interface PhotoRepository {
    Long create(PhotoEntity p);
    List<PhotoEntity> findByListingId(Long listingId);
    Optional<PhotoEntity> findById(Long id);
    void delete(Long id);
}
