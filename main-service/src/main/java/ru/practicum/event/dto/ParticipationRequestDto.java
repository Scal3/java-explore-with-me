package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.event.enums.RequestState;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    private Long id;

    private Long event;

    private Long requester;

    private LocalDateTime created;

    private RequestState status;
}
