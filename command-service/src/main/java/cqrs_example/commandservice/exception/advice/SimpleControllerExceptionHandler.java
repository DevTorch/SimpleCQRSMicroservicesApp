package cqrs_example.commandservice.exception.advice;

import cqrs_example.commandservice.exception.KafkaSenderException;
import cqrs_example.commandservice.exception.SimpleEntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class SimpleControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors =new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return errors;
    }

    @ExceptionHandler(SimpleEntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleSimpleEntityNotFoundException(SimpleEntityNotFoundException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorMessage", e.getMessage());
        errors.put("timestamp", LocalDateTime.now().toString());

        return  errors;
    }

    @ExceptionHandler(KafkaSenderException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, String> handleKafkaSenderException(KafkaSenderException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorMessage", e.getMessage());
        errors.put("timestamp", LocalDateTime.now().toString());

        return  errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(Exception e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("errorMessage", e.getMessage());
        errors.put("timestamp", LocalDateTime.now().toString());

        return  errors;
    }

}
