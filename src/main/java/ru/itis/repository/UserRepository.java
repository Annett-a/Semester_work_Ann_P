package ru.itis.repository;

import ru.itis.model.UserEntity;
import java.util.Optional;

public interface UserRepository {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    void create(UserEntity user);
}
