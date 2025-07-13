package ru.untitled_devs.core.utils;

import org.apache.tika.Tika;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class FileUtils {
    private static final Tika tika = new Tika();

    private static String parseMimeExt(String mimeType) {
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("Provided file is not an image: " + mimeType);
        }

        String ext = switch (mimeType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpg";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            default -> "";
        };

        return ext;
    }

	public static String getImageFileNameWithExtension(byte[] data) {
		String mimeType = tika.detect(data);
		return UUID.randomUUID() + parseMimeExt(mimeType);
	}

	public static String getImageFileNameWithExtension(InputStream data) throws IOException {
		String mimeType = tika.detect(data);
		return UUID.randomUUID() + parseMimeExt(mimeType);
	}

	public static String getImageFileExtension(byte[] data) {
		String mimeType = tika.detect(data);
		return parseMimeExt(mimeType);
	}

	public static String getImageFileExtension(InputStream data) throws IOException {
		String mimeType = tika.detect(data);
		return parseMimeExt(mimeType);
	}
}
