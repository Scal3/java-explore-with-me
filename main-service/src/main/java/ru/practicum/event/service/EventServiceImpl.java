package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.entity.CategoryEntity;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.*;
import ru.practicum.event.entity.EventEntity;
import ru.practicum.event.entity.LocationEntity;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.error.ForbiddenException;
import ru.practicum.exception.error.NotFoundException;
import ru.practicum.user.entity.UserEntity;
import ru.practicum.user.service.UserService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;

    private final LocationRepository locationRepository;

    private final CategoryService categoryService;

    private final UserService userService;

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
        log.info("Entering createEvent: userId = {}, NewEventDto = {}", userId, dto);
        LocalDateTime eventStartDate = LocalDateTime.parse(dto.getEventDate(), formatter);

        if (!isEventStartDateCorrect(eventStartDate)) {
            throw new ForbiddenException(
                    "For the requested operation the conditions are not met.",
                    "Field: eventDate. " +
                            "Error: должно содержать дату, которая еще не наступила. Value: " +
                            eventStartDate
            );
        }

        CategoryEntity categoryEntity =
                mapper.map(categoryService.getCategoryById(dto.getCategory()), CategoryEntity.class);
        UserEntity userEntity = mapper.map(userService.getUserById(userId), UserEntity.class);
        LocationEntity locationEntity =
                getOrCreateLocationEntity(dto.getLocation().getLat(), dto.getLocation().getLon());
        EventEntity eventEntity = EventEntity.builder()
                        .annotation(dto.getAnnotation())
                        .createdOn(LocalDateTime.now())
                        .description(dto.getDescription())
                        .eventDate(eventStartDate)
                        .paid(dto.getPaid())
                        .participantLimit(dto.getParticipantLimit())
                        .requestModeration(dto.getRequestModeration())
                        .state(EventState.PENDING)
                        .title(dto.getTitle())
                        .views(0)
                        .category(categoryEntity)
                        .initiator(userEntity)
                        .location(locationEntity)
                        .build();
        eventRepository.save(eventEntity);
        EventFullDto result = mapper.map(eventEntity, EventFullDto.class);
        log.info("Exiting createEvent");

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getUserEvent(long userId, long eventId) {
        log.info("Entering getUserEvent: userId = {}, eventId = {}", userId, eventId);
        Optional<EventEntity> optionalEventEntity =
                eventRepository.findByIdAndInitiatorId(eventId, userId);

        if (optionalEventEntity.isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "Event with id=" + eventId + " was not found");
        }

        EventFullDto result = mapper.map(optionalEventEntity.get(), EventFullDto.class);
        log.info("Exiting getUserEvent");

        return result;
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
        return eventStartDate.isAfter(LocalDateTime.now().plusHours(2));
    }

    @Transactional
    private LocationEntity getOrCreateLocationEntity(Double lat, Double lon) {
        Optional<LocationEntity> locationEntityOptional =
                locationRepository.findByLatAndLon(lat, lon);

        if (locationEntityOptional.isPresent()) {
            return locationEntityOptional.get();
        }

        LocationEntity locationEntity = LocationEntity.builder()
                .lat(lat)
                .lon(lon)
                .build();
        locationRepository.save(locationEntity);

        return locationEntity;
    }
}
