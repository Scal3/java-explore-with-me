package ru.practicum.event.service;

import ru.practicum.event.dto.ParticipationRequestDto;
import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getUserRequests(long userId);

    ParticipationRequestDto saveUserRequest(long userId, long eventId);

    ParticipationRequestDto cancelUserRequest(long userId, long requestId);
}
