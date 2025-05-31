package ru.untitled_devs.core.fsm.context;

import ru.untitled_devs.core.fsm.State;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FSMContext {

    private State state = new State("Default");
    private final Map<DataKey<?>, Object> data = new HashMap<>();

    public FSMContext() {}

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public <T> void setData(DataKey<T> key, T value) {
        data.put(key, value);
    }

    public <T> T getData(DataKey<T> key) {
        Object raw = data.get(key);
        if (raw == null) {
            return null;
        }
        return key.getType().cast(raw);
    }

    public boolean hasData() {
        return !data.isEmpty();
    }

    public <T> void removeData(DataKey<T> key) {
        this.data.remove(key);
    }

    public Set<DataKey<?>> getDataKeys() {
        return this.data.keySet();
    }

    public Map<DataKey<?>, Object> getAllData() {
        return Collections.unmodifiableMap(this.data);
    }

    public void clearData() {
        this.data.clear();
    }

    public void reset() {
        this.state = new State("Default");
        this.data.clear();
    }
}
