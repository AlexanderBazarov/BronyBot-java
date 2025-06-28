package ru.untitled_devs.core.executor;

import org.apache.logging.log4j.Logger;
import ru.untitled_devs.core.utils.NamedThreadFactory;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserQueueExecutor implements UserScopedExecutor{
	NamedThreadFactory namedThreadFactory = new NamedThreadFactory("ExecutorThread-");
	private final ExecutorService executor;
	private final Map<Long, Queue<Runnable>> queues = new ConcurrentHashMap<>();
	private final Set<Long> active = ConcurrentHashMap.newKeySet();
	private final Logger logger;

	public UserQueueExecutor(int poolSize, Logger logger) {
		this.executor =  Executors.newFixedThreadPool(poolSize, namedThreadFactory);
		this.logger = logger;
	}

	@Override
	public void submit(long chatId, Runnable task) {
		queues.computeIfAbsent(chatId, __ -> new ConcurrentLinkedQueue<>()).add(task);
		tryStart(chatId);
	}

	private void tryStart(long chatId) {
		if (!active.add(chatId)) return;

		executor.submit(() -> {
			try {
				while (true) {
					Runnable task = queues.getOrDefault(chatId, new ConcurrentLinkedQueue<>()).poll();
					if (task == null) break;

					try {
						task.run();
					} catch (Exception e) {
						logger.error(e);
					}
				}
			} finally {
				active.remove(chatId);
				if (!queues.getOrDefault(chatId, new ConcurrentLinkedQueue<>()).isEmpty()) {
					tryStart(chatId);
				}
			}
		});
	}
}
