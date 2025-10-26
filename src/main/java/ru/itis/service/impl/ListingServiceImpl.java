package ru.itis.service.impl;

import ru.itis.model.ListingEntity;
import ru.itis.repository.ListingRepository;
import ru.itis.repository.TagRepository;
import ru.itis.service.ListingService;
import ru.itis.validation.ListingValidationService;

import java.util.List;
import java.util.Optional;

public class ListingServiceImpl implements ListingService {
    private final ListingRepository repo;
    private final ListingValidationService validator;
    private final TagRepository tagRepo;

    public ListingServiceImpl(ListingRepository repo,
                              ListingValidationService validator,
                              TagRepository tagRepo) {
        this.repo = repo;
        this.validator = validator;
        this.tagRepo = tagRepo;
    }

    @Override public List<ListingEntity> all() { return repo.findAll(); }
    @Override public List<ListingEntity> byAuthorId(Long userId) { return repo.findAllByAuthorId(userId); }
    @Override public Optional<ListingEntity> byId(Long id) { return repo.findById(id); }

    @Override
    public Long create(ListingEntity e, List<Long> tagIds) {
        var err = validator.validate(e);
        if (!err.isEmpty()) throw new IllegalArgumentException("Invalid listing: " + err);
        Long id = repo.create(e);
        tagRepo.replaceTags(id, tagIds);
        return id;
    }

    @Override
    public void updateOwn(ListingEntity e, Long userId, List<Long> tagIds) {
        if (!repo.isOwner(e.getId(), userId)) throw new SecurityException("Нет прав на редактирование");
        var err = validator.validate(e);
        if (!err.isEmpty()) throw new IllegalArgumentException("Invalid listing: " + err);
        repo.update(e);
        tagRepo.replaceTags(e.getId(), tagIds);
    }

    @Override
    public void deleteOwn(Long id, Long userId) {
        if (!repo.isOwner(id, userId)) throw new SecurityException("Нет прав на удаление");
        repo.delete(id);
    }
}
