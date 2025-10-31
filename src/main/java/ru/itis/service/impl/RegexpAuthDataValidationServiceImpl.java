package ru.itis.service.impl;

import ru.itis.validation.AuthDataValidationService;
import java.util.HashMap;
import java.util.Map;

public class RegexpAuthDataValidationServiceImpl implements AuthDataValidationService {
    @Override
    public Map<String, String> validate(String email, String password) {
        var errors = new HashMap<String, String>();
        if (email == null || email.isBlank() || !email.matches(".+@.+\\..+"))
            errors.put("email", "Введите корректный e-mail");
        if (password == null || password.length() < 4)
            errors.put("password", "Минимум 4 символа");
        return errors;
    }
}
