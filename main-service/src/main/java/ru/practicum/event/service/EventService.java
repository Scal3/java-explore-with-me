package ru.practicum.event.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.*;
import java.util.List;

public interface EventService {

    List<EventShortDto> getUsersEvents(long userId, Pageable pageable);

    EventFullDto createEvent(long userId, NewEventDto dto);

    EventFullDto getUserEvent(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto);

    // TODO Получение информации о запросах на участие в событии текущего пользователя

    // TODO Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя

    List<EventFullDto> getEvents(GetEventsFullDto dto);

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest dto);

    List<EventShortDto> getEvents(GetEventsShortDto dto);

    EventFullDto getEventById(long eventId);
}