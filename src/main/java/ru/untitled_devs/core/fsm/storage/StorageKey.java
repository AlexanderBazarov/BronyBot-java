package ru.untitled_devs.core.fsm.storage;

import java.util.Objects;

public class StorageKey {
    private final long chatId;
    private final long userId;

    public StorageKey(long chatId, long userId) {
        this.chatId = chatId;
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, userId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StorageKey that = (StorageKey) o;
        return chatId == that.chatId && userId == that.userId;
    }
}
