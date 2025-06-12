package ru.untitled_devs.core.fsm.states;

import java.lang.reflect.Field;
import java.util.*;

public abstract class StatesGroup {
    private static final Map<Class<?>, List<State>> cache = new HashMap<>();

    protected static State state() {
        return null;
    }

    public static List<State> allStates(Class<? extends StatesGroup> cls) {
        return cache.computeIfAbsent(cls, (clazz) -> {
            List<State> result = new ArrayList<>();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType().equals(State.class)) {
                    field.setAccessible(true);
                    try {
                        State val = (State) field.get(null);
                        if (val == null) {
                            String name = clazz.getSimpleName() + ":" + field.getName();
                            State s = new State(name);
                            field.set(null, s);
                            result.add(s);
                        } else {
                            result.add(val);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
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
}