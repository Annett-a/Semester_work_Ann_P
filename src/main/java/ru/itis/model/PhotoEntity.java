package ru.itis.model;

import lombok.Data;

@Data
public class PhotoEntity {
    private Long id;
    private Long listingId;
    private String fileName;
    private String storagePath;
    private String contentType;
    private long size;
}
