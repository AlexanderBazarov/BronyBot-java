package ru.untitled_devs.core.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadFactory implements ThreadFactory {
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private String threadNamePrefix = "";

	public NamedThreadFactory(String threadlNamePrefix) {
		this.threadNamePrefix = threadlNamePrefix;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		return new Thread(runnable, threadNamePrefix + threadNumber.getAndIncrement());
	}
}
