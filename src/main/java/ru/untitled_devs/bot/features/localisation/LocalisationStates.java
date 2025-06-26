package ru.untitled_devs.bot.features.localisation;

import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.fsm.states.StatesGroup;

public class LocalisationStates extends StatesGroup {
	public static State START = state(LocalisationStates.class, "Start");
	public static State GETLANG = state(LocalisationStates.class, "GETLANG");
}
