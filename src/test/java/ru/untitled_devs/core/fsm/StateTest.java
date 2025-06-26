package ru.untitled_devs.core.fsm;

import org.junit.jupiter.api.Test;
import ru.untitled_devs.core.fsm.states.State;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class StateTest {

    @Test
    void getName_ShouldReturnConstructorValue() {
        String stateName = "Active";
        State state = new State(stateName);

        String result = state.getName();

        assertEquals(stateName, result, "getName() should return the name passed to the constructor");
    }

    @Test
    void equals_ShouldReturnTrueForSameName() {
        State state1 = new State("Pending");
        State state2 = new State("Pending");

        assertEquals(state1, state2, "equals() should return true for two State objects with the same name");
        assertEquals(state2, state1, "equals() should be symmetric: state2.equals(state1) must also be true");
        assertEquals(state1.hashCode(), state2.hashCode(),
                "hashCode() should be equal for two State objects that are equal");
    }

    @Test
    void equals_ShouldReturnFalseForDifferentName() {

        State state1 = new State("Started");
        State state2 = new State("Stopped");

        assertNotEquals(state1, state2, "equals() should return false for State objects with different names");
    }

    @Test
    void equals_ShouldReturnFalseWhenComparedWithNullOrDifferentClass() {
        State state = new State("Running");

        assertNotEquals(null, state, "equals() should return false when compared to null");
        assertNotEquals(new State("some string"), state, "equals() should return false when compared to an object of a different class");
    }

    @Test
    void hashCode_ShouldBeConsistentForSameInstance() {
        State state = new State("Completed");

        int firstHash = state.hashCode();
        int secondHash = state.hashCode();

        assertEquals(firstHash, secondHash, "hashCode() should return the same value on multiple invocations for the same instance");
    }

    @Test
    void hashCode_ShouldDifferForDifferentNames() {
        State state1 = new State("Alpha");
        State state2 = new State("Beta");

        int hash1 = state1.hashCode();
        int hash2 = state2.hashCode();

        assertNotEquals(hash1, hash2,
                "hashCode() should ideally differ for State objects with different names");
    }
}
