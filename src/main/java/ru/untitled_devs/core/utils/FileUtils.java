package ru.untitled_devs.core.utils;

import org.apache.tika.Tika;

import java.util.UUID;

public class FileUtils {
    private static final Tika tika = new Tika();

    public static String getImageFileNameWithExtension(byte[] data) {
        String mimeType = tika.detect(data);

        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("Provided file is not an image: " + mimeType);
        }

        String ext = switch (mimeType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> ".bin";
        };

        return UUID.randomUUID() + ext;
    }
}
