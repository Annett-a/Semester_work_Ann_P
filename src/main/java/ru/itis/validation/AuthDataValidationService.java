package ru.itis.validation;

import java.util.Map;

public interface AuthDataValidationService {
    Map<String, String> validate(String email, String password);
}
