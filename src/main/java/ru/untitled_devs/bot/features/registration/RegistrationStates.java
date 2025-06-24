package ru.untitled_devs.bot.features.registration;

import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.fsm.states.StatesGroup;

public final class RegistrationStates extends StatesGroup {
    public static State START = state(RegistrationStates.class, "Start");
    public static State NAME = state(RegistrationStates.class, "Name");
    public static State AGE = state(RegistrationStates.class, "Age");
    public static State LOCATION = state(RegistrationStates.class, "Location");
	public static State DESCRIPTION = state(RegistrationStates.class, "Description");
    public static State PHOTO = state(RegistrationStates.class, "Photo");
	public static State PREVIEW = state(RegistrationStates.class, "Preview");
	public static State FINISH = state(RegistrationStates.class, "Finish");
}
