package ru.untitled_devs.core.executor;

@FunctionalInterface
public interface UserScopedExecutor {
	void submit(long userId, Runnable task);
}
