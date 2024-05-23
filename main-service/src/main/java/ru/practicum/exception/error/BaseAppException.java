package ru.practicum.exception.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class BaseAppException extends ResponseStatusException {

    private final String message;

    public BaseAppException(HttpStatus status, String reason, String message) {
        super(status, reason);
        this.message =  message;
    }
}