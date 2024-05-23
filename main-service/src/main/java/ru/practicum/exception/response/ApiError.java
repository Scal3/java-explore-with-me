package ru.practicum.exception.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ApiError {

    private final HttpStatus status;

    private final String reason;

    private final String message;

    private final String timestamp;

}
