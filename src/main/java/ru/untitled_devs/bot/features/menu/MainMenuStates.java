package ru.untitled_devs.bot.features.menu;

import ru.untitled_devs.core.fsm.states.State;
import ru.untitled_devs.core.fsm.states.StatesGroup;

public class MainMenuStates extends StatesGroup {
	public static State MENU = state(MainMenuStates.class, "Menu");
}
