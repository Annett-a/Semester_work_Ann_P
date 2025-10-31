package ru.itis.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import ru.itis.model.UserEntity;
import ru.itis.repository.UserRepository;
import ru.itis.service.AuthService;
import ru.itis.validation.AuthDataValidationService;

public class AuthServiceImpl implements AuthService {
    private final UserRepository users;
    private final AuthDataValidationService validator;

    public AuthServiceImpl(UserRepository users, AuthDataValidationService validator) {
        this.users = users;
        this.validator = validator;
    }

    @Override
    public boolean authenticate(String email, String rawPassword) {
        return users.findByEmail(email)
                .map(u -> BCrypt.checkpw(rawPassword, u.getPassword()))
                .orElse(false);
    }

    @Override
    public void register(String email, String rawPassword, String fullName) {
        var errors = validator.validate(email, rawPassword);
        if (!errors.isEmpty()) throw new IllegalArgumentException("Invalid data: " + errors);
        if (users.existsByEmail(email)) throw new IllegalStateException("Email уже зарегистрирован");
        var u = new UserEntity();
        u.setEmail(email);
        u.setPassword(BCrypt.hashpw(rawPassword, BCrypt.gensalt()));
        u.setFullName(fullName);
        users.create(u);
    }
}
