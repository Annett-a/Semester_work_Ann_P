package ru.itis.service;

import ru.itis.model.ListingEntity;

import java.util.List;
import java.util.Optional;

public interface ListingService {
    List<ListingEntity> all();
    List<ListingEntity> byAuthorId(Long userId);
    Optional<ListingEntity> byId(Long id);

    Long create(ListingEntity e, List<Long> tagIds);
    void updateOwn(ListingEntity e, Long userId, List<Long> tagIds);
    void deleteOwn(Long id, Long userId);
}
