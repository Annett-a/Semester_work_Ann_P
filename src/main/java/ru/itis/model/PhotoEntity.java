package ru.itis.model;

import lombok.Data;

@Data
public class PhotoEntity {
    private Long id;
    private Long listingId;
    private String fileName;     // исходное имя
    private String storagePath;  // относительный путь от baseDir
    private String contentType;
    private long size;
}
