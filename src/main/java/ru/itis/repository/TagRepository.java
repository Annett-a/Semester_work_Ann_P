package ru.itis.repository;

import ru.itis.model.TagEntity;

import java.util.List;

public interface TagRepository {
    List<TagEntity> findAll();
    List<TagEntity> findByListingId(Long listingId);
    void replaceTags(Long listingId, List<Long> tagIds);
}
