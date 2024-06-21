package ru.practicum.event.dto;

import lombok.*;
import ru.practicum.event.enums.EventState;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetEventsFullDto {
    private List<Long> users;

    private List<Long> categories;

    private List<EventState> states;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    private Integer from;

    private Integer size;
}
