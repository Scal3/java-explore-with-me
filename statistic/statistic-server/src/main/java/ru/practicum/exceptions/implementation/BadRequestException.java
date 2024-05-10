package ru.practicum.exceptions.implementation;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseAppException {

    public BadRequestException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
