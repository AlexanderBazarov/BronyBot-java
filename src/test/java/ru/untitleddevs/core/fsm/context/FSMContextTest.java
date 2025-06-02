package ru.untitleddevs.core.fsm.context;

import org.junit.jupiter.api.Test;
import ru.untitled_devs.core.fsm.State;
import ru.untitled_devs.core.fsm.context.DataKey;
import ru.untitled_devs.core.fsm.context.FSMContext;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FSMContextTest {
    String expectedName = "Alex";
    Class<String> expectedType = String.class;
    private final String stateName = "Test";

    FSMContext getContext() {
        return new FSMContext();
    }

    State getTestState() {
        return new State(stateName);
    }

    DataKey<String> getDataKey() {
        return DataKey.of(expectedName, expectedType);
    }

    @Test
    void setStateAndGetStateTestReturnValidState() {
        FSMContext context = getContext();
        context.setState(getTestState());

        State state = context.getState();
        assertEquals(state, getTestState(), "Method should return the expected state");
    }

    @Test
    void setStateAndGetStateTestReturnInvalidState() {
        FSMContext context = getContext();
        context.setState(getTestState());

        State state = context.getState();
        assertNotEquals(new State("Invalid State"), state, "Method should  not expected state");
    }

    @Test
    void setDataAndGetDataTestReturnEqualData() {
        FSMContext context = getContext();
        DataKey<String> key = getDataKey();
        context.setData(key, expectedName);

        String name = context.getData(key);

        assertEquals(expectedName, name, "Method should return equal data");
    }

    @Test
    void setDataAndGetDataTestReturnNotEqualData() {
        FSMContext context = getContext();
        DataKey<String> key = getDataKey();
        context.setData(key, "Sona");

        String name = context.getData(key);

        assertNotEquals(expectedName, name, "Method should return not equal data");
    }

    @Test
    void getDataGotInvalidKey() {
        FSMContext context = getContext();
        DataKey<String> key = getDataKey();
        context.setData(key, expectedName);

        DataKey<Integer> anotherKey = DataKey.of("Age", Integer.class);
        assertNull(context.getData(anotherKey));
    }

    @Test
    void hasDataReturnsTrue() {
        FSMContext context = getContext();
        DataKey<String> key = getDataKey();
        context.setData(key, expectedName);

        assertTrue(context.hasData(), "Method should return True");
    }

    @Test
    void hasDataReturnsFalse() {
        FSMContext context = getContext();
        assertFalse(context.hasData(), "Method should return False");
    }

    @Test
    void removeDataRemovesData() {
        FSMContext context = getContext();

        DataKey<String> key = getDataKey();
        context.setData(key, expectedName);

        DataKey<Integer> anotherKey = DataKey.of("Age", Integer.class);
        context.setData(anotherKey, 21);

        context.removeData(anotherKey);

        assertNull(context.getData(anotherKey), "Method should remove data by key");
        assertEquals(expectedName, context.getData(key), "Method should not remove or edit another keys");
    }

    @Test
    void removeDataGotInvalidKey() {
        FSMContext context = getContext();
        DataKey<String> missingKey = getDataKey();

        assertDoesNotThrow(() -> context.removeData(missingKey), "Method should not throw any exceptions");
    }

    @Test
    void getDataKeysShouldNotReturnNull() {
        FSMContext context = getContext();
        assertNotNull(context.getDataKeys());
    }

    @Test
    void getDataKeysReturnsAllKeys() {
        FSMContext context = getContext();

        DataKey<String> key = getDataKey();
        context.setData(key, expectedName);

        DataKey<Integer> anotherKey = DataKey.of("Age", Integer.class);
        context.setData(anotherKey, 21);

        Set<DataKey<?>> expectedKeys = new HashSet<>();
        expectedKeys.add(key);
        expectedKeys.add(anotherKey);

        Set<DataKey<?>> keys = context.getDataKeys();

        assertTrue(keys.containsAll(expectedKeys));
    }

    @Test
    void getAllDataReturnsAllValidData() {
        FSMContext context = getContext();

        DataKey<String> key = getDataKey();
        context.setData(key, expectedName);

        DataKey<Integer> anotherKey = DataKey.of("Age", Integer.class);
        context.setData(anotherKey, 21);

        Map<DataKey<?>, Object> data = context.getAllData();

        assertTrue(data.containsKey(key), "Data should contain key");
        assertTrue(data.containsKey(key), "Data should contain anotherKey");

        assertEquals(expectedName, data.get(key), "Data should contain the key for expectedName");
        assertEquals(21, data.get(anotherKey), "Returned data should contain the key for age");

        assertEquals(2, data.size(), "Data size should be 2");

    }

    @Test
    void clearDataClearsAllData() {
        FSMContext context = getContext();

        DataKey<String> key = getDataKey();
        context.setData(key, expectedName);

        DataKey<Integer> anotherKey = DataKey.of("Age", Integer.class);
        context.setData(anotherKey, 21);

        context.clearData();

        assertEquals(0, context.getAllData().size(), "Data size should be 0");
    }

    @Test
    void resetShouldClearAllDataAndResetStateToDefault() {
        FSMContext context = getContext();

        context.setState(getTestState());

        DataKey<String> key = getDataKey();
        context.setData(key, expectedName);

        context.reset();

        assertEquals(new State("Default"), context.getState(), "State should be Default");
        assertEquals(0, context.getAllData().size());
        assertNotNull(context.getAllData(), "Data should not be null");
    }

}
