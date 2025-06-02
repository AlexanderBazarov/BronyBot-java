package ru.untitled_devs.core.fsm.storage;

import ru.untitled_devs.core.fsm.context.FSMContext;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorage implements Storage {
    private final ConcurrentHashMap<StorageKey, FSMContext> store = new ConcurrentHashMap<>();

    @Override
    public FSMContext getContext(StorageKey key) {
        return this.store.get(key);
    }

    @Override
    public void setContext(StorageKey key, FSMContext context) {
        this.store.put(key, context);
    }

    public FSMContext getOrCreateContext(StorageKey key) {
        return store.computeIfAbsent(key, k -> new FSMContext());
    }
}
