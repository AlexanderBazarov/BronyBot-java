package ru.untitled_devs.core.fsm.storage;


import ru.untitled_devs.core.fsm.context.FSMContext;

public interface Storage {
    FSMContext getContext(StorageKey key);
    void setContext(StorageKey key, FSMContext context);
    FSMContext getOrCreateContext(StorageKey key);
}
