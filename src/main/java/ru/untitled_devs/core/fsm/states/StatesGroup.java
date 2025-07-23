package ru.untitled_devs.core.fsm.states;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class StatesGroup {
	private static final Logger logger = LogManager.getLogger(StatesGroup.class);
    private static final Map<Class<?>, List<State>> cache = new ConcurrentHashMap<>();

    protected static State state(Class<? extends StatesGroup> groupClass, String name) {
        return new State(groupClass.getSimpleName() + ":" + name);
    }

    public static List<State> allStates(Class<? extends StatesGroup> cls) {
        return cache.computeIfAbsent(cls, (clazz) -> {
            List<State> result = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType().equals(State.class)) {
                    field.setAccessible(true);
                    try {
                        State val = (State) field.get(null);

						result.add(val);

                    } catch (IllegalAccessException e) {
						logger.error(e);
                    }
                }
            }
            return result;
        });
    }

    public static State first(Class<? extends StatesGroup> cls) {
        return allStates(cls).stream().findFirst().orElse(null);
    }

    public static State next(Class<? extends StatesGroup> cls, State current) {
        List<State> states = allStates(cls);
        int i = states.indexOf(current);
        return (i >= 0 && i < states.size() - 1) ? states.get(i + 1) : null;
    }

	public static boolean contains(Class<? extends StatesGroup> cls, State state) {
		return allStates(cls).contains(state);
	}
}
