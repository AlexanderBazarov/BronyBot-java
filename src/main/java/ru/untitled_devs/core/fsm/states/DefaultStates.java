package ru.untitled_devs.core.fsm.states;

public class DefaultStates extends StatesGroup {
    public static State DEFAULT = state();

    static {
        allStates(DefaultStates.class);
    }
}
