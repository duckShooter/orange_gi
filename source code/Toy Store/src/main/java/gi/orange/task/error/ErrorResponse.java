package gi.orange.task.error;

import java.time.LocalDateTime;

/*
 * A simple class to represent an error message, same as the default one used by Spring exception handlers
 */
public final class ErrorResponse {
	private final LocalDateTime timeStamp;
	private final int status;
	private final String error;
	private final String message;
	private final String path;
	
	public ErrorResponse(int status, String error, String message, String path) {
		timeStamp = LocalDateTime.now();
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public int getStatus() {
		return status;
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}
}
