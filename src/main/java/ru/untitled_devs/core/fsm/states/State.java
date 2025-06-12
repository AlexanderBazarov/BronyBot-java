package ru.untitled_devs.core.fsm.states;

import java.util.Objects;

public class State {
    private final String name;

    public State(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(name, state.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
