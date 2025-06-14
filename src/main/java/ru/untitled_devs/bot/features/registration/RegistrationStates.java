package ru.untitled_devs.bot.features.registration;

import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.fsm.states.StatesGroup;

public final class RegistrationStates extends StatesGroup {
    public static final State START = state();
    public static final State NAME = state();
    public static final State AGE = state();
    public static final State LOCATION = state();
    public static final State PHOTO = state();

    static {
        allStates(RegistrationStates.class);
    }
}
