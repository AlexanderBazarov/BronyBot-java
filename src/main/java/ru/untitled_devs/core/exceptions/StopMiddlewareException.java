package ru.untitled_devs.core.exceptions;

public class StopMiddlewareException extends RuntimeException {
	public StopMiddlewareException() {
		super();
	}

	public StopMiddlewareException(String message) {
		super(message);
	}
}
