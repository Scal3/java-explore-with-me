package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.ParticipationRequestDto;
import ru.practicum.event.entity.EventEntity;
import ru.practicum.event.entity.RequestEntity;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.enums.RequestState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.RequestRepository;
import ru.practicum.exception.error.ForbiddenException;
import ru.practicum.exception.error.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.entity.UserEntity;
import ru.practicum.user.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    private final UserService userService;

    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getUserRequests(long userId) {
        log.info("Entering getUserRequests: userId = {}", userId);
        List<RequestEntity> requestEntities =
                requestRepository.findByRequesterIdOrderByCreatedDesc(userId);
        List<ParticipationRequestDto> participationRequestDtos =
                mapper.map(requestEntities,
                        new TypeToken<List<ParticipationRequestDto>>() {}.getType());
        log.info("Exiting getUserRequests");

        return participationRequestDtos;
    }

    @Transactional
    @Override
    public ParticipationRequestDto saveUserRequest(long userId, long eventId) {
        log.info("Entering saveUserRequest: userId = {}, eventId = {}", userId, eventId);
        UserDto userDto = userService.getUserById(userId);
        Optional<EventEntity> eventEntityOptional = eventRepository.findById(eventId);

        if (eventEntityOptional.isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "Event with id=" + eventId + " was not found");
        }

        EventEntity eventEntity = eventEntityOptional.get();
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);

        if (eventEntity.getInitiator().getId() == userId) {
            throw new ForbiddenException(
                    "For the requested operation the conditions are not met.",
                    "Event initiator can not make a request"
            );
        }

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ForbiddenException(
                    "For the requested operation the conditions are not met.",
                    "You can not make a second request"
            );
        }

        if (!eventEntity.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException(
                    "For the requested operation the conditions are not met.",
                    "You can not participate at an unpublished event"
            );
        }

        boolean isLimitReached = eventEntity.getRequests().stream()
                .filter(r -> !r.getStatus().equals(RequestState.REJECTED))
                .count() == eventEntity.getParticipantLimit();

        if (isLimitReached) {
            throw new ForbiddenException(
                    "For the requested operation the conditions are not met.",
                    "The event participant limit is reached"
            );
        }

        RequestEntity requestEntity = RequestEntity.builder()
                        .requester(userEntity)
                        .event(eventEntity)
                        .status(eventEntity.getRequestModeration()
                                ? RequestState.PENDING
                                : RequestState.CONFIRMED)
                        .created(LocalDateTime.now())
                        .build();
        requestRepository.save(requestEntity);
        ParticipationRequestDto participationRequestDto =
                mapper.map(requestEntity, ParticipationRequestDto.class);
        log.info("Exiting saveUserRequest");

        return participationRequestDto;
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelUserRequest(long userId, long requestId) {
        log.info("Entering cancelUserRequest: userId = {}, requestId = {}",
                userId, requestId);
        userService.getUserById(userId);
        Optional<RequestEntity> requestEntityOptional =
                requestRepository.findById(requestId);

        if (requestEntityOptional.isEmpty()) {
            throw new NotFoundException(
                    "The required object was not found.",
                    "Event with id=" + requestId + " was not found");
        }

        RequestEntity requestEntity = requestEntityOptional.get();
        requestEntity.setStatus(RequestState.REJECTED);
        requestRepository.save(requestEntity);
        ParticipationRequestDto participationRequestDto =
                mapper.map(requestEntity, ParticipationRequestDto.class);
        log.info("Exiting cancelUserRequest");

        return participationRequestDto;
    }
}
