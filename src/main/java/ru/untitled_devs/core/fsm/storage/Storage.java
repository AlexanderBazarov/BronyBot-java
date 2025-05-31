package ru.untitled_devs.core.fsm.storage;


import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.StorageKey;

public interface Storage {
    public FSMContext getContext(StorageKey key);
    public void setContext(StorageKey key, FSMContext context);
    public FSMContext getOrCreateContext(StorageKey key);
}
