package ru.practicum.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.implementation.BadRequestException;
import ru.practicum.exceptions.response.ErrorResponse;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ErrorResponse handleError(BadRequestException e) {
        log.warn("Error has occurred {}", e.getDescription());

        return new ErrorResponse(
                e.getCode(),
                e.getDescription(),
                LocalDateTime.now()
        );
    }
}
