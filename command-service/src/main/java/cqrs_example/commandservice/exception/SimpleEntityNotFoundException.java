package cqrs_example.commandservice.exception;

public class SimpleEntityNotFoundException extends RuntimeException {
    public SimpleEntityNotFoundException() {
    }

    public SimpleEntityNotFoundException(String message) {
        super(message);
    }

    public SimpleEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
