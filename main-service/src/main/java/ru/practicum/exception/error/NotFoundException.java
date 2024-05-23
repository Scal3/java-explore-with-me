package ru.practicum.exception.error;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseAppException {

    public NotFoundException(String reason, String message) {
        super(HttpStatus.NOT_FOUND, reason, message);
    }
}
