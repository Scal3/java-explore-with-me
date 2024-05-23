package ru.practicum.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.exception.error.BadRequestException;
import ru.practicum.exception.error.NotFoundException;
import ru.practicum.exception.response.ApiError;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestControllerAdvice
public class MainExceptionHandler {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ApiError handleError(BadRequestException e) {
        log.warn("Error has occurred {}", e.getMessage());

        return new ApiError(
                e.getStatus(),
                e.getReason(),
                e.getMessage(),
                LocalDateTime.now().format(DATE_TIME_FORMATTER)
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleError(NotFoundException e) {
        log.warn("Error has occurred {}", e.getMessage());

        return new ApiError(
                e.getStatus(),
                e.getReason(),
                e.getMessage(),
                LocalDateTime.now().format(DATE_TIME_FORMATTER)
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiError handleError(DataIntegrityViolationException e) {
        log.warn("Error has occurred {}", e.getMessage());

        return new ApiError(
                HttpStatus.CONFLICT,
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now().format(DATE_TIME_FORMATTER)
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiError handleError(MethodArgumentNotValidException e) {
        log.warn("Error has occurred {}", e.getMessage());

        return new ApiError(
                HttpStatus.BAD_REQUEST,
                "Incorrectly made request.",
                e.getMessage(),
                LocalDateTime.now().format(DATE_TIME_FORMATTER)
        );
    }
}
