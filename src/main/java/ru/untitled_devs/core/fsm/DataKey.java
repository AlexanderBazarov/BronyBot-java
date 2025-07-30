package ru.untitled_devs.core.fsm;

import java.util.Objects;

public final class DataKey<T> {
    private final String name;
    private final Class<T> type;

    public DataKey(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public static <T> DataKey<T> of(String name, Class<T> type) {
        return new DataKey<>(name, type);
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataKey<?> that)) return false;
        return name.equals(that.name) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}

