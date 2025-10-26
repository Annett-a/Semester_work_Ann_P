package ru.itis.config;

import java.io.File;

public final class FileStorageConfig {
    private static final File BASE_DIR;

    static {
        String dir = System.getenv("UPLOAD_DIR");
        if (dir == null || dir.trim().isEmpty()) {
            dir = System.getProperty("user.home") + File.separator + "darom_uploads";
        }
        BASE_DIR = new File(dir).getAbsoluteFile();
        if (!BASE_DIR.exists() && !BASE_DIR.mkdirs()) {
            throw new RuntimeException("Cannot create upload dir: " + BASE_DIR);
        }
    }

    private FileStorageConfig() {}

    public static File baseDir() {
        return BASE_DIR;
    }
}
