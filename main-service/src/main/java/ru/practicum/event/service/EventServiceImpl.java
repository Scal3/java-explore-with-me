package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.entity.CategoryEntity;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.*;
import ru.practicum.event.entity.EventEntity;
import ru.practicum.event.entity.LocationEntity;
import ru.practicum.event.enums.EventAdminAction;
import ru.practicum.event.enums.EventSort;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.EventUserAction;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.error.ForbiddenException;
import ru.practicum.exception.error.NotFoundException;
import ru.practicum.user.entity.UserEntity;
import ru.practicum.user.service.UserService;
import ru.practicum.util.CopyNonNullProperties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;

    private final LocationRepository locationRepository;

    private final CategoryService categoryService;

    private final UserService userService;

    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getUserEvents(long userId, Pageable pageable) {
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
                        .state(dto.getRequestModeration() ? EventState.PENDING : EventState.PUBLISHED)
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
        log.info("Entering updateEvent: userId = {}, eventId = {}, UpdateEventUserRequest = {}",
                userId, eventId, dto);
        Optional<EventEntity> eventEntityOptional = eventRepository.findById(eventId);

        if (eventEntityOptional.isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "Event with id=" + eventId + " was not found");
        }

        EventEntity eventEntity = eventEntityOptional.get();

        if (eventEntity.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException(
                    "For the requested operation the conditions are not met.",
                    "Only pending or canceled events can be changed"
            );
        }

        if (dto.getStateAction().equals(EventUserAction.CANCEL_REVIEW)) {
            eventEntity.setState(EventState.CANCELED);
        } else {
            CopyNonNullProperties.copyNonNullProperties(dto, eventEntity, (src, target) -> {
                handleLocationConvert(src, target);
                handleEventDateConvert(src, target);
                handleCategoryConvert(src, target);
            }, "location", "category", "eventDate");

            if (!isEventStartDateCorrect(eventEntity.getEventDate())) {
                throw new ForbiddenException(
                        "For the requested operation the conditions are not met.",
                        "Field: eventDate. " +
                                "Error: должно содержать дату, которая еще не наступила. Value: " +
                                eventEntity.getEventDate()
                );
            }
        }

        eventRepository.save(eventEntity);
        EventFullDto result = mapper.map(eventEntity, EventFullDto.class);
        log.info("Exiting updateEvent");

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getEvents(GetEventsFullDto dto) {
        log.info("Entering getEvents: GetEventsFullDto = {}", dto);
        List<EventEntity> eventEntities = eventRepository.findByFilters(dto.getUsers(),
                dto.getCategories(),
                dto.getStates(),
                LocalDateTime.parse(dto.getRangeStart(), formatter),
                LocalDateTime.parse(dto.getRangeEnd(), formatter),
                PageRequest.of(dto.getFrom() / dto.getSize(), dto.getSize()));
        List<EventFullDto> result =
                mapper.map(eventEntities, new TypeToken<List<EventFullDto>>() {}.getType());
        log.info("Exiting getEvents");

        return result;
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest dto) {
        log.info("Entering updateEvent: eventId = {}, UpdateEventAdminRequest = {}", eventId, dto);
        Optional<EventEntity> eventEntityOptional = eventRepository.findById(eventId);

        if (eventEntityOptional.isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "Event with id=" + eventId + " was not found");
        }

        EventEntity eventEntity = eventEntityOptional.get();

        if (!eventEntity.getState().equals(EventState.PENDING)) {
            String eventName = dto.getStateAction().equals(EventAdminAction.PUBLISH_EVENT)
                    ? "publish"
                    : "reject";

            throw new ForbiddenException(
                    "For the requested operation the conditions are not met.",
                    "Cannot " + eventName + " the event because it's not in the right state: PUBLISHED"
            );
        }

        if (dto.getStateAction().equals(EventAdminAction.REJECT_EVENT)) {
            eventEntity.setState(EventState.CANCELED);
        } else {
            CopyNonNullProperties.copyNonNullProperties(dto, eventEntity, (src, target) -> {
                handleLocationConvert(src, target);
                handleEventDateConvert(src, target);
                handleCategoryConvert(src, target);
            }, "location", "category", "eventDate");
            eventEntity.setPublishedOn(LocalDateTime.now());

            if (eventEntity.getEventDate().plusHours(1).isBefore(eventEntity.getPublishedOn())) {
                throw new ForbiddenException(
                        "For the requested operation the conditions are not met.",
                        "Field: eventDate. " +
                                "Error: дата начала изменяемого события должна быть " +
                                "не ранее чем за час от даты публикации. Value: " +
                                eventEntity.getEventDate()
                );
            }
        }

        eventRepository.save(eventEntity);
        EventFullDto result = mapper.map(eventEntity, EventFullDto.class);
        log.info("Exiting updateEvent");

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getEvents(GetEventsShortDto dto) {
        // TODO do it when add request module
        //  информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
        log.info("Entering getEvents: GetEventsShortDto = {}", dto);
        List<EventEntity> eventEntities;

        if (dto.getSort().equals(EventSort.EVENT_DATE)) {
            eventEntities = eventRepository.findByFiltersOrderByEventDateDesc(
                    dto.getText(),
                    dto.getCategories(),
                    dto.getPaid(),
                    dto.getOnlyAvailable(),
                    LocalDateTime.parse(dto.getRangeStart(), formatter),
                    LocalDateTime.parse(dto.getRangeEnd(), formatter),
                    PageRequest.of(dto.getFrom() / dto.getSize(), dto.getSize()));
        } else {
            eventEntities = eventRepository.findByFiltersOrderByViewsDesc(
                    dto.getText(),
                    dto.getCategories(),
                    dto.getPaid(),
                    dto.getOnlyAvailable(),
                    LocalDateTime.parse(dto.getRangeStart(), formatter),
                    LocalDateTime.parse(dto.getRangeEnd(), formatter),
                    PageRequest.of(dto.getFrom() / dto.getSize(), dto.getSize()));
        }

        List<EventShortDto> result =
                mapper.map(eventEntities, new TypeToken<List<EventShortDto>>() {}.getType());
        log.info("Exiting getEvents");

        return result;
    }

    @Transactional
    @Override
    public EventFullDto getEventById(long eventId) {
        // TODO do it when add request module
        //  информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
        log.info("Entering getEventById: eventId = {}", eventId);
        Optional<EventEntity> eventEntityOptional =
                eventRepository.findByIdAndState(eventId, EventState.PUBLISHED);

        if (eventEntityOptional.isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "Event with id=" + eventId + " was not found");
        }

        EventEntity eventEntity = eventEntityOptional.get();
        eventEntity.setViews(eventEntity.getViews() + 1);
        eventRepository.save(eventEntity);
        EventFullDto result = mapper.map(eventEntity, EventFullDto.class);
        log.info("Exiting getEventById");

        return result;
    }

    private boolean isEventStartDateCorrect(LocalDateTime eventStartDate) {
        return eventStartDate.isAfter(LocalDateTime.now().plusHours(2));
    }

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

    private void handleEventDateConvert(Object src, Object target) {
        UpdateEventUserRequest dto = (UpdateEventUserRequest) src;
        EventEntity eventEntity = (EventEntity) target;

        String eventDate = dto.getEventDate();
        if (eventDate != null) {
            LocalDateTime eventStartDate = LocalDateTime.parse(dto.getEventDate(), formatter);
            eventEntity.setEventDate(eventStartDate);
        }
    }

    private void handleLocationConvert(Object src, Object target) {
        UpdateEventUserRequest dto = (UpdateEventUserRequest) src;
        EventEntity eventEntity = (EventEntity) target;

        LocationDto locationDto = dto.getLocation();
        if (locationDto != null) {
            LocationEntity locationEntity =
                    getOrCreateLocationEntity(dto.getLocation().getLat(), dto.getLocation().getLon());
            eventEntity.setLocation(locationEntity);
        }
    }

    private void handleCategoryConvert(Object src, Object target) {
        UpdateEventUserRequest dto = (UpdateEventUserRequest) src;
        EventEntity eventEntity = (EventEntity) target;

        Integer categoryId = dto.getCategory();
        if (categoryId != null) {
            CategoryEntity categoryEntity =
                    mapper.map(categoryService.getCategoryById(dto.getCategory()), CategoryEntity.class);
            eventEntity.setCategory(categoryEntity);
        }
    }
}
