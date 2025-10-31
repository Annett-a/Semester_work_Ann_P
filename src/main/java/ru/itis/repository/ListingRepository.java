package ru.itis.repository;

import ru.itis.model.ListingEntity;
import java.util.List;
import java.util.Optional;

public interface ListingRepository {
    List<ListingEntity> findAll();
    List<ListingEntity> findAllByAuthorId(Long userId);
    Optional<ListingEntity> findById(Long id);
    Long create(ListingEntity e);
    void update(ListingEntity e);
    void delete(Long id);
    boolean isOwner(Long id, Long userId);
}
