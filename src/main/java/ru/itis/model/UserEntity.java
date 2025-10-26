package ru.itis.model;

import lombok.Data;

@Data
public class UserEntity {
    private Long id;
    private String email;
    private String password; // хранится хэш
    private String fullName;
}
