package ru.untitleddevs.core.fsm.storage;

import org.junit.jupiter.api.Test;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;
import ru.untitled_devs.core.fsm.storage.InMemoryStorage;
import ru.untitled_devs.core.fsm.storage.StorageKey;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryStorageTest {

    @Test
    void getContextShouldReturnNull_WhenNoContextSet() {
        InMemoryStorage storage = new InMemoryStorage();
        StorageKey key = new StorageKey(1L, 1L);

        FSMContext result = storage.getContext(key);

        assertNull(result, "getContext() should return null if no context has been set for the given key");
    }

    @Test
    void setContextThenGetContext_ShouldReturnSameContext() {
        InMemoryStorage storage = new InMemoryStorage();
        StorageKey key = new StorageKey(2L, 3L);
        FSMContext context = new FSMContext();

        storage.setContext(key, context);
        FSMContext retrieved = storage.getContext(key);

        assertNotNull(retrieved, "getContext() should not return null after setContext()");
        assertSame(context, retrieved, "getContext() should return the exact same instance that was passed to setContext()");
    }

    @Test
    void getOrCreateContextShouldCreateNewContext_WhenAbsent() {
        InMemoryStorage storage = new InMemoryStorage();
        StorageKey key = new StorageKey(4L, 5L);

        FSMContext firstRetrieval = storage.getOrCreateContext(key);

        assertNotNull(firstRetrieval, "getOrCreateContext() should create a new FSMContext when none exists");
        assertEquals("Default", firstRetrieval.getState().getName(),
                "Newly created FSMContext should have its state initialized to \"Default\"");
    }

    @Test
    void getOrCreateContextShouldReturnSameInstance_OnMultipleCalls() {
        InMemoryStorage storage = new InMemoryStorage();
        StorageKey key = new StorageKey(6L, 7L);

        FSMContext firstCall = storage.getOrCreateContext(key);
        FSMContext secondCall = storage.getOrCreateContext(key);

        assertNotNull(firstCall, "First call to getOrCreateContext() should return a non-null FSMContext");
        assertSame(firstCall, secondCall, "Subsequent calls to getOrCreateContext() with the same key should return the same instance");
    }

    @Test
    void getOrCreateContextShouldReturnExistingContext_IfAlreadySetWithSetContext() {
        InMemoryStorage storage = new InMemoryStorage();
        StorageKey key = new StorageKey(8L, 9L);
        FSMContext customContext = new FSMContext();
        customContext.setData(DataKey.of("test", String.class), "value");

        storage.setContext(key, customContext);
        FSMContext retrievedViaGetOrCreate = storage.getOrCreateContext(key);

        assertSame(customContext, retrievedViaGetOrCreate,
                "getOrCreateContext() should return the instance previously set via setContext()");
    }

    @Test
    void setContextShouldOverwriteExistingContext() {
        InMemoryStorage storage = new InMemoryStorage();
        StorageKey key = new StorageKey(10L, 11L);
        FSMContext firstContext = new FSMContext();
        FSMContext secondContext = new FSMContext();

        storage.setContext(key, firstContext);
        storage.setContext(key, secondContext);
        FSMContext result = storage.getContext(key);

        assertSame(secondContext, result,
                "setContext() should overwrite the previously stored FSMContext for the same key");
    }
}
