package ru.untitled_devs.core.fsm.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StorageKeyTest {
    int expectedChatId = 12345;
    int expectedUserId = 6789;

    StorageKey getStorageKey() {
        return new StorageKey(expectedChatId, expectedUserId);
    }

    @Test
    void equalsGotEqualObject() {
        StorageKey key = getStorageKey();
        assertEquals(new StorageKey(expectedChatId, expectedUserId), key, "Keys should be equals");
    }

    @Test
    void equalsGotNotEqualObject() {
        StorageKey key = getStorageKey();
        assertNotEquals(new StorageKey(151242, 3575467), key, "Keys should not be equals");
    }

    @Test
    void equalsGotNotEqualChatId() {
        StorageKey key = getStorageKey();
        assertNotEquals(new StorageKey(151242, expectedUserId), key, "Keys should not be equals");
    }

    @Test
    void equalsGotNotEqualUserId() {
        StorageKey key = getStorageKey();
        assertNotEquals(new StorageKey(expectedChatId, 15462342), key, "Keys should not be equals");
    }

    @Test
    void hashCodeShouldBeSameForEqualObjects() {
        StorageKey key1 = new StorageKey(55555L, 99999L);
        StorageKey key2 = new StorageKey(55555L, 99999L);

        assertEquals(key1.hashCode(), key2.hashCode(), "hashCode must be identical for objects that are equal");
    }

    @Test
    void hashCodeShouldBeDifferentForDifferentObjects() {
        StorageKey key1 = new StorageKey(11111L, 22222L);
        StorageKey key2 = new StorageKey(33333L, 44444L);

        assertNotEquals(key1.hashCode(), key2.hashCode(), "hashCode should differ for objects with different chatId and userId");
    }
}
