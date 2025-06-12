package ru.untitled_devs.core.fsm.context;
import ru.untitled_devs.core.fsm.states.DefaultStates;
import ru.untitled_devs.core.fsm.states.State;

import java.util.*;

public class FSMContext {
    private State state;
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
        Object rawData= data.get(key);
        if (rawData == null) {
            return null;
        }
        return key.getType().cast(rawData);
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
        this.state = DefaultStates.DEFAULT;
        this.data.clear();
    }

}
