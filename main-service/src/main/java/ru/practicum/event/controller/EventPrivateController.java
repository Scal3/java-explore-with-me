package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class EventPrivateController {

    private final EventService eventService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}")
    public List<EventShortDto> getUserEvents(
            @PathVariable @Positive long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("Entering getUserEvents: userId = {}, from = {}, size = {} ", userId, from, size);
        List<EventShortDto> eventShortDtos =
                eventService.getUserEvents(userId, PageRequest.of(from / size, size));
        log.info("Exiting getUserEvents");

        return eventShortDtos;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(
            @PathVariable @Positive long userId,
            @RequestBody @Valid NewEventDto dto
    ) {
        log.info("Entering createEvent: userId = {}, NewEventDto = {}", userId, dto);
        EventFullDto eventFullDto = eventService.createEvent(userId, dto);
        log.info("Exiting createEvent");

        return eventFullDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(
            @PathVariable @Positive long userId,
            @PathVariable @Positive long eventId
    ) {
        log.info("Entering getUserEvent: userId = {}, eventId = {}", userId, eventId);
        EventFullDto eventFullDto = eventService.getUserEvent(userId, eventId);
        log.info("Exiting getUserEvent");

        return eventFullDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable @Positive long userId,
            @PathVariable @Positive long eventId,
            @RequestBody @Valid UpdateEventUserRequest dto
    ) {
        log.info("Entering updateEvent: userId = {}, eventId = {}, UpdateEventUserRequest = {}",
                userId, eventId, dto);
        EventFullDto eventFullDto = eventService.updateEvent(userId, eventId, dto);
        log.info("Exiting updateEvent");

        return eventFullDto;
    }

//    @ResponseStatus(HttpStatus.OK)
//    @GetMapping("/{userId}/events/{eventId}/requests")
//    public EventFullDto getUserEventRequests(
//            @PathVariable @Positive long userId,
//            @PathVariable @Positive long eventId
//    ) {
//
//    }

//    @ResponseStatus(HttpStatus.OK)
//    @PatchMapping("/{userId}/events/{eventId}/requests")
//    public EventFullDto updateUserEventRequests(
//            @PathVariable @Positive long userId,
//            @PathVariable @Positive long eventId,
//            @RequestBody @Valid ...
//    ) {
//
//    }
}
