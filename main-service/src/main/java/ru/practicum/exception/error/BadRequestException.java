package ru.practicum.exception.error;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseAppException {

    public BadRequestException(String reason, String message) {
        super(HttpStatus.BAD_REQUEST, reason, message);
    }
}
