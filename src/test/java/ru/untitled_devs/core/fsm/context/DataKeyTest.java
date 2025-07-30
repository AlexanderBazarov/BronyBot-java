package ru.untitled_devs.core.fsm.context;

import org.junit.jupiter.api.Test;
import ru.untitled_devs.core.fsm.DataKey;

import static org.junit.jupiter.api.Assertions.*;

public class DataKeyTest {
    String expectedName = "age";
    Class<Integer> expectedType = Integer.class;

    DataKey<Integer> getDataKey() {
        return DataKey.of(expectedName, expectedType);
    }

    @Test
    void ofCreatesNonNullInstance() {
        DataKey<Integer> key = getDataKey();
        assertNotNull(key, "Method should not return null");
    }

    @Test
    void getNameAndTypeReturnValuesPassedToOf() {
        DataKey<Integer> key = getDataKey();
        assertEquals(expectedName, key.getName(), "Method should return ");
        assertEquals(expectedType, key.getType(), "Method should return the expected type");
    }

    @Test
    void equalsGotEqualObject() {
        DataKey<Integer> key1 = getDataKey();
        DataKey<Integer> key2 = DataKey.of(expectedName, expectedType);

        assertEquals(key1, key2, "equals() should return true for objects with same name and type");
        assertEquals(key1.hashCode(), key2.hashCode(), "hashCode() should be equal objects");
    }

    @Test
    void equalsGotNotEqualObject() {
        DataKey<Integer> key1 = getDataKey();
        DataKey<String> key2 = DataKey.of("name", String.class);

        assertNotEquals(key1, key2, "equals() should return true for objects with same name and type");
        assertNotEquals(key1.hashCode(), key2.hashCode(), "hashCode() should be equal objects");
    }

}
