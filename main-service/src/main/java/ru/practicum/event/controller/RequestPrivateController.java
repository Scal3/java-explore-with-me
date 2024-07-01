package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.ParticipationRequestDto;
import ru.practicum.event.service.RequestService;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestPrivateController {

    private final RequestService requestService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable @Positive long userId) {
        log.info("Entering getUserRequests: userId = {}", userId);
        List<ParticipationRequestDto> participationRequestDtos =
                requestService.getUserRequests(userId);
        log.info("Exiting getUserRequests");

        return participationRequestDtos;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto saveUserRequests(
            @PathVariable @Positive long userId,
            @RequestParam @Positive long eventId
    ) {
        log.info("Entering saveUserRequests: userId = {}, eventId = {}", userId, eventId);
        ParticipationRequestDto participationRequestDto =
                requestService.saveUserRequest(userId, eventId);
        log.info("Exiting saveUserRequests");

        return participationRequestDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelUserRequest(
            @PathVariable @Positive long userId,
            @PathVariable @Positive long requestId
    ) {
        log.info("Entering cancelUserRequest: userId = {}, requestId = {}", userId, requestId);
        ParticipationRequestDto participationRequestDto =
                requestService.cancelUserRequest(userId, requestId);
        log.info("Exiting cancelUserRequest");

        return participationRequestDto;
    }
}
