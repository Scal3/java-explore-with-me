package ru.practicum.exception.error;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseAppException {

    public ForbiddenException(String reason, String message) {
        super(HttpStatus.FORBIDDEN, reason, message);
    }
}
