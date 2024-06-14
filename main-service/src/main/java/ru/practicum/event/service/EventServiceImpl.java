package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.*;
import ru.practicum.event.entity.EventEntity;
import ru.practicum.event.repository.EventRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;

    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getUsersEvents(long userId, Pageable pageable) {
        log.info("Entering getUsersEvents: userId = {}, Pageable = {}", userId, pageable);
        List<EventEntity> events =
                eventRepository.findByInitiatorIdOrderByViewsDesc(userId, pageable);
        List<EventShortDto> dtos =
                mapper.map(events, new TypeToken<List<EventShortDto>>() {}.getType());
        log.info("Exiting getUsersEvents");

        return dtos;
    }

    @Transactional
    @Override
    public EventFullDto createEvent(long userId, NewEventDto dto) {
        // Обратите внимание: дата и время на которые намечено событие не может быть раньше,
        // чем через два часа от текущего момента
        log.info("Entering createEvent: userId = {}, NewEventDto = {}", userId, dto);
        LocalDateTime eventStartDate = LocalDateTime.parse(dto.getEventDate(), formatter);

        if (!isEventStartDateCorrect(eventStartDate)) {
            // TODO throw an exception FORBIDDEN
        }

        log.info("Exiting createEvent");

        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getUserEvent(long userId, long eventId) {
        return null;
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest dto) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getEvents(GetEventsFullDto dto) {
        return null;
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest dto) {
        return null;
    }

    // TODO I have to increase views here
    // каждый публичный запрос для получения списка событий или полной информации о мероприятии
    // должен фиксироваться сервисом статистики
    @Transactional
    @Override
    public List<EventShortDto> getEvents(GetEventsShortDto dto) {
        return null;
    }

    // TODO I have to increase views here
    // каждый публичный запрос для получения списка событий или полной информации о мероприятии
    // должен фиксироваться сервисом статистики
    @Transactional
    @Override
    public EventFullDto getEventById(long eventId) {
        return null;
    }

    private boolean isEventStartDateCorrect(LocalDateTime eventStartDate) {
        return true;
    }
}
