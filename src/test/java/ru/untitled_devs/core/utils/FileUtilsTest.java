package ru.untitled_devs.core.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;

public class FileUtilsTest {
    @Test
    void getImageFileNameWithExtensionGotValidPng() {
        byte[] data = Base64.getDecoder().decode(
                "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVQI12NgYAAAAAMAASsJ"
        );
        String filename = FileUtils.getImageFileNameWithExtension(data);
        assertTrue(filename.endsWith(".png"));
    }

    @Test
    void getImageFileNameWithExtensionGotValidJpeg() {
        byte[] data = Base64.getDecoder().decode(
                "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAP//////////////////////////////////////////////////////////////////////////////////////" +
                        "2wBDAf//////////////////////////////////////////////////////////////////////////////////////" +
                        "wAARCAABAAEDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAb/xAAUEQEAAAAAAAAAAAAAAAAAAAAA/8QAFQEBAQAAAAAAAAAAAAAAAAAAAgP/" +
                        "xAAUEQEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwD9AP/Z"
        );
        String filename = FileUtils.getImageFileNameWithExtension(data);
        assertTrue(filename.endsWith(".jpg"));
    }

    @Test
    void getImageFileNameWithExtensionGotValidGif() {
        byte[] data = Base64.getDecoder().decode(
                "R0lGODdhAQABAIAAAAAAAP///ywAAAAAAQABAAACAkQBADs="
        );
        String filename = FileUtils.getImageFileNameWithExtension(data);
        assertTrue(filename.endsWith(".gif"));
    }

    @Test
    void getImageFileNameWithExtensionGotValidWebp() {
        byte[] data = Base64.getDecoder().decode(
                "UklGRhIAAABXRUJQVlA4TAYAAAAvAAAAHAAAQQUAAA=="
        );
        String filename = FileUtils.getImageFileNameWithExtension(data);
        assertTrue(filename.endsWith(".webp"));
    }

    @Test
    void getImageFileNameWithExtensionGotInvalidData() {
        byte[] data = Base64.getDecoder().decode("SGVsbG8gd29ybGQ=");

        assertThrows(IllegalArgumentException.class,
                () -> FileUtils.getImageFileNameWithExtension(data));
    }

}
