package org.example.repo2cloud.exception;

import lombok.Getter;

/**
 * Thrown when communication with the Build Server fails for reasons
 * beyond normal HTTP error responses (e.g. connection refused, timeouts,
 * deserialization failures).
 *
 * <p>Handled by {@link GlobalExceptionHandler} and converted into a
 * clean API error response.</p>
 */
@Getter
public class BuildServerCommunicationException extends RuntimeException {

    private final int statusCode;

    public BuildServerCommunicationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public BuildServerCommunicationException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public BuildServerCommunicationException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 503;
    }
}
