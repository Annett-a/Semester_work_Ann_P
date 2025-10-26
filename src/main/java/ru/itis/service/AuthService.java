package ru.itis.service;

public interface AuthService {
    boolean authenticate(String email, String rawPassword);
    void register(String email, String rawPassword, String fullName);
}
