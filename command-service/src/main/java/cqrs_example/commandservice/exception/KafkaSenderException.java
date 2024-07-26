package cqrs_example.commandservice.exception;

import java.time.LocalDateTime;

public class KafkaSenderException extends RuntimeException {
    public KafkaSenderException(LocalDateTime timestamp, String message) {
        super(message);
    }

    public KafkaSenderException(String message) {
        super(message);
    }

    public KafkaSenderException(String message, Throwable cause) {
        super(message, cause);
    }
}
