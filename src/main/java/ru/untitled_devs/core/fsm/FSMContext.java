package ru.untitled_devs.core.fsm;

import ru.untitled_devs.core.fsm.states.DefaultStates;
import ru.untitled_devs.core.fsm.states.State;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FSMContext {
    private State state = DefaultStates.DEFAULT;
    private final Map<DataKey<?>, Object> data = new HashMap<>();
	private final Map<DataKey<?>, Object> sceneData = new HashMap<>();
	private String sceneId;

    public FSMContext() {

	}

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
        data.remove(key);
    }

    public Set<DataKey<?>> getDataKeys() {
        return data.keySet();
    }

    public Map<DataKey<?>, Object> getAllData() {
        return Collections.unmodifiableMap(data);
    }

    public void clearData() {
        data.clear();
    }

    public void reset() {
        state = DefaultStates.DEFAULT;
        data.clear();
    }

	public void setSceneId(String sceneId) {
		this.sceneId = sceneId;
	}

	public String getSceneId() {
		return sceneId;
	}

	public void clearScene() {
		sceneId = null;
	}

	public void clearWizard() {
		sceneId = null;
		sceneData.clear();
	}

	public <T> void setScenedData(DataKey<T> key, T value) {
		sceneData.put(key, value);
	}

	public <T> T getScenedData(DataKey<T> key) {
		Object rawData= data.get(key);
		if (rawData == null) {
			return null;
		}
		return key.getType().cast(rawData);
	}

	public void resetScenedData() {
		sceneData.clear();
	}

}
