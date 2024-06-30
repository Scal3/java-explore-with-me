package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.GetEventsFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.service.EventService;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class EventAdminController {

    private final EventService eventService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        log.info("Entering getEvents: users = {}, states = {}, categories = {}, " +
                        "rangeStart = {}, rangeEnd = {}, from = {}, size = {} ",
                users, states, categories, rangeStart, rangeEnd, from, size);
        GetEventsFullDto dto = GetEventsFullDto.builder()
                        .users(users)
                        .categories(categories)
                        .states(states)
                        .rangeStart(rangeStart)
                        .rangeEnd(rangeEnd)
                        .from(from)
                        .size(size)
                        .build();
        List<EventFullDto> eventFullDtos = eventService.getEvents(dto);
        log.info("Exiting getEvents");

        return eventFullDtos;
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{eventId}")
    public EventFullDto updateEventById(
            @PathVariable @Positive long eventId,
            @RequestBody UpdateEventAdminRequest dto
    ) {
        log.info("Entering updateEventById: eventId = {}, ", eventId);
        EventFullDto eventFullDto = eventService.updateEvent(eventId, dto);
        log.info("Exiting updateEventById");

        return eventFullDto;
    }
}
