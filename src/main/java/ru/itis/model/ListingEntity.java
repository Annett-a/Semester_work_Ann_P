package ru.itis.model;

import lombok.Data;

@Data
public class ListingEntity {
    private Long id;
    private String title;
    private String type;
    private String status;
    private Long authorId;
}
