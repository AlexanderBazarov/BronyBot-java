package ru.untitled_devs.core.routers.filters;

import ru.untitled_devs.core.context.UpdateContext;

@FunctionalInterface
public interface Filter {
    boolean check(UpdateContext update);
}
