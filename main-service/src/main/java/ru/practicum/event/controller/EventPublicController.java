package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CreateStatisticDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.GetEventsShortDto;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.service.EventService;
import ru.practicum.stats.client.StatisticClient;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final StatisticClient statisticClient;

    private final EventService eventService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<EventShortDto> getEvents(
            HttpServletRequest request,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) EventSort sort,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "10") int size

    ) {
        log.info("Entering getEvents");
        GetEventsShortDto dto = GetEventsShortDto.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build();
        List<EventShortDto> eventShortDtos = eventService.getEvents(dto);
        statisticClient.saveStatistic(
                new CreateStatisticDto(
                        "main-service",
                        "/events",
                        request.getRemoteAddr(),
                        LocalDateTime.now().toString()));
        log.info("Exiting getEvents");

        return eventShortDtos;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public EventFullDto getEventById(HttpServletRequest request, @PathVariable @Positive long id) {
        log.info("Entering getEventById: id = {}", id);
        EventFullDto eventFullDto = eventService.getEventById(id);
        statisticClient.saveStatistic(
                new CreateStatisticDto(
                        "main-service",
                        "/events/{id}",
                        request.getRemoteAddr(),
                        LocalDateTime.now().toString()));
        log.info("Exiting getEventById");

        return eventFullDto;
    }
}